package ru.profiles.ui.registration

import android.Manifest
import android.app.Activity.RESULT_OK
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
import ru.profiles.viewmodel.LoginViewModel
import ru.profiles.viewmodel.RegistrationViewModel
import javax.inject.Inject

import android.widget.Toast
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegistrationFragment2 : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var regViewModel: RegistrationViewModel

    companion object {
        val PICK_GALLERY_IMAGE = 0
        val PICK_CAMERA_IMAGE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.registration_fragment_2, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.show()
        a.supportActionBar?.title = ""
        v.avatar_image.setOnClickListener {
            createChooser(a)
        }
        regViewModel = ViewModelProviders.of(this, viewModelFactory)[RegistrationViewModel::class.java]
        return v
    }

    private fun createChooser(ctx: Context){
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(ctx.getString(R.string.image_chooser_text))
        builder.setItems(ctx.resources.getStringArray(R.array.image_chooser_items)) { d, w ->
            when (w) {
                0 -> {
                    runGallery()
                }
                1 -> {
                    runCamera(ctx)
                }
                else -> {}
            }
        }
        builder.show()
    }

    private fun runGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, PICK_GALLERY_IMAGE)
    }

    var mPhotoUri: Uri? = null

    private fun runCamera(ctx: Context){
        // todo check camera feature
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

            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_reg_frag_2_to_imageEditorFragment)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var mCropImageUri: Uri? = null

    /*@SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(context!!, data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(context!!, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                )
            } else {
                // no permissions required or already granted, can start crop image activity
                //CropImage.activity(imageUri).start(activity!!)
                startCropImageActivity(imageUri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri ?: return )
            } else {
                Toast.makeText(activity!!, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show()
            }
        }
    }*/

    var mCurrentPhotoPath: String? = null

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
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = absolutePath
            }
        }
    }

    private fun startCropImageActivity(imageUri: Uri) {

    }
}