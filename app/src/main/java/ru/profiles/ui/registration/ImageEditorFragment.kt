package ru.profiles.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.image_editor_fragment.view.*
import ru.profiles.profiles.R
import ru.profiles.viewmodel.RegistrationViewModel
import javax.inject.Inject

class ImageEditorFragment: DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var regViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.image_editor_fragment, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.show()
        a.supportActionBar?.title = ""
        regViewModel = ViewModelProviders.of(this, viewModelFactory)[RegistrationViewModel::class.java]
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regViewModel.mLocalPicUri?.let{view.main_image.setImageURI(it)}
    }

}