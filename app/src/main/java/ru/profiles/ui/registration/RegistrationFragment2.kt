package ru.profiles.ui.registration

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
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.registration_fragment_2.view.*
import ru.profiles.profiles.R

class RegistrationFragment2 : DaggerFragment() {

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

    private fun runCamera(ctx: Context){
        // todo check camera feature
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                startActivityForResult(takePictureIntent, PICK_CAMERA_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // todo run editor
        super.onActivityResult(requestCode, resultCode, data)
    }
}