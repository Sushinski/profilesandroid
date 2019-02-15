package ru.profiles.ui.registration

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.registration_fragment_2.view.*
import ru.profiles.profiles.R
import ru.profiles.viewmodel.RegistrationViewModel
import javax.inject.Inject
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.registration_fragment_2.*
import okhttp3.MediaType
import okhttp3.RequestBody
import ru.profiles.extensions.disableBackButton
import ru.profiles.viewmodel.LoginViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegistrationFragment2 : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var regViewModel: RegistrationViewModel

    private var mPhotoUri: Uri? = null

    companion object {
        const val PICK_GALLERY_IMAGE = 0
        const val PICK_CAMERA_IMAGE = 1
        const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.registration_fragment_2, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        a.supportActionBar?.show()
        this.disableBackButton()
        v.avatar_image.setOnClickListener {
            createChooser(a)
        }
        regViewModel = ViewModelProviders.of(this, viewModelFactory)[RegistrationViewModel::class.java]
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            RegistrationFragment2Args.fromBundle(it).imageUri?.let {
                    u->regViewModel.mLocalPicUri = Uri.parse(u)
            }
        }
        regViewModel.getLocalPicUri().observe(this, androidx.lifecycle.Observer {
                v->avatar_image.setImageURI(v.toString())
                set_photo_text.visibility = if(v == null) View.VISIBLE else View.INVISIBLE
        })

        enter_button.setOnClickListener {
            regViewModel.mLocalPicUri?.let {
                    u->activity?.contentResolver?.openInputStream(u)
            }?.let{
                s->
                s.use {
                    r -> regViewModel.saveUserImage(RequestBody.create(MediaType.parse("image/*"), r.readBytes()))
                    upload_progress.visibility = View.VISIBLE
                }
            }
        }

        regViewModel.getPicUploadStatus().observe(this, Observer {
            success-> if(success) NavHostFragment.findNavController(this).navigate(R.id.action_reg_frag_2_to_main)
            upload_progress.visibility = View.INVISIBLE
        })

    }


    private fun createChooser(ctx: Context){
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(ctx.getString(R.string.image_chooser_text))
        builder.setAdapter(DialogAdapter(ctx, ctx.resources.getStringArray(
            regViewModel.mLocalPicUri?.let{R.array.image_change_items} ?: R.array.image_chooser_items))){
                _, w ->
            when (w) {
                0 -> runGallery()
                1 -> runCamera(ctx)
                2 -> regViewModel.mLocalPicUri = null
                else -> {}
            }
        }
        builder.show()
    }

    private fun runGallery(){
        if (!ensureExternalStoragePermissionGranted()) return
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_GALLERY_IMAGE)
    }

     private fun ensureExternalStoragePermissionGranted(): Boolean {
         context?.let{
                 c->ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE)
         }?.let {
             p ->
             return if (p != PackageManager.PERMISSION_GRANTED) {
                 requestPermissions(
                     arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                     PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
                 false
             } else true
         }
     }

    private fun runCamera(ctx: Context){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(ctx)
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also { file ->
                    mPhotoUri = FileProvider.getUriForFile(
                        ctx,
                        "ru.profiles.profiles.fileprovider",
                        file
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                    startActivityForResult(takePictureIntent, PICK_CAMERA_IMAGE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            regViewModel.mLocalPicUri = when(requestCode){
                PICK_GALLERY_IMAGE->{ data?.data }
                PICK_CAMERA_IMAGE->{mPhotoUri}
                else->null
            }
            val ad = RegistrationFragment2Directions.actionRegFrag2ToImageEditorFragment()
            ad.imageUri = regViewModel.mLocalPicUri.toString()
            NavHostFragment
                .findNavController(this)
                .navigate(ad)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runGallery()
            } else {
                Toast.makeText(activity!!, "Для продолжения требуется разрешение!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private var mCurrentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(ctx: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("rus")).format(Date())
        val storageDir: File? = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return storageDir?.let{
            File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                mCurrentPhotoPath = absolutePath
            }
        }
    }

    internal class DialogAdapter(ctx: Context, items: Array<String>)
        : ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, items){

        private val mChooserItems = items

        override fun getView(position: Int, convertView: View?, parent: ViewGroup) : View  {
            val view: View = super.getView(position, convertView, parent)
            val text1: TextView = view.findViewById(android.R.id.text1) as TextView
            val t = mChooserItems[position]
            text1.text = t
            @Suppress("DEPRECATION")
            text1.setTextColor(
                context.resources.getColor(
                    if(t == context.resources.getString(R.string.delete_photo)) R.color.colorAccent //todo
                    else android.R.color.widget_edittext_dark
                )
            )
            return view
        }
    }
}