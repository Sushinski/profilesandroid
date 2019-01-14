package ru.profiles.ui.registration

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.image_editor_fragment.view.*
import ru.profiles.profiles.R

class ImageEditorFragment: DaggerAppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.image_editor_fragment, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.show()
        a.supportActionBar?.title = ""
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            view.main_image.setImageURI(Uri.parse(ImageEditorFragmentArgs.fromBundle(it).imageUri))
        }
    }

}