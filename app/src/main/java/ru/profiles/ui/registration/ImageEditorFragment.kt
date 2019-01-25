package ru.profiles.ui.registration

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.image_editor_fragment.*
import kotlinx.android.synthetic.main.image_editor_fragment.view.*
import ru.profiles.profiles.R
import ru.profiles.utils.Edge
import ru.profiles.utils.ImageViewUtil
import ru.profiles.viewmodel.ImageEditViewModel
import java.io.File
import java.io.IOException
import javax.inject.Inject


class ImageEditorFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ImageEditViewModel

    private var mSaveUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.image_editor_fragment, container, false)

        val a = (activity as AppCompatActivity)
        a.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        a.supportActionBar?.hide()
        v.done_fab.setOnClickListener {
            val cropped = getCroppedImage()
            val uri = viewModel.saveCroppedFile(a.applicationContext, cropped)
            val action = ImageEditorFragmentDirections.actionImageEditorFragmentToRegFrag2()
            action.imageUri = uri?.toString() ?: ""
            NavHostFragment.findNavController(this).navigate(action)
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ImageEditViewModel::class.java]
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            val h = GenericDraweeHierarchyBuilder
                .newInstance(context?.resources)
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build()
            photo_drawee_view.hierarchy = h
            mSaveUri = Uri.parse(ImageEditorFragmentArgs.fromBundle(it).imageUri)
            photo_drawee_view.setPhotoUri(mSaveUri)

        }
    }

    private fun getCroppedImage(): Bitmap {

        val mCurrentDisplayedBitmap = getCurrentDisplayedImage()
        val displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mCurrentDisplayedBitmap, photo_drawee_view)

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for width.
        val actualImageWidth = mCurrentDisplayedBitmap.width.toFloat()
        val displayedImageWidth = displayedImageRect.width().toFloat()
        val scaleFactorWidth = actualImageWidth / displayedImageWidth

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for height.
        val actualImageHeight = mCurrentDisplayedBitmap.height.toFloat()
        val displayedImageHeight = displayedImageRect.height().toFloat()
        val scaleFactorHeight = actualImageHeight / displayedImageHeight

        // Get crop window position relative to the displayed image.
        val cropWindowX = Edge.LEFT.coordinate - displayedImageRect.left
        val cropWindowY = Edge.TOP.coordinate - displayedImageRect.top
        val cropWindowWidth = Edge.getWidth()
        val cropWindowHeight = Edge.getHeight()

        // Scale the crop window position to the actual size of the Bitmap.
        val actualCropX = cropWindowX * scaleFactorWidth
        val actualCropY = cropWindowY * scaleFactorHeight
        val actualCropWidth = cropWindowWidth * scaleFactorWidth
        val actualCropHeight = cropWindowHeight * scaleFactorHeight

        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(
            mCurrentDisplayedBitmap,
            actualCropX.toInt(),
            actualCropY.toInt(),
            actualCropWidth.toInt(),
            actualCropHeight.toInt()
        )
    }


    private fun getCurrentDisplayedImage(): Bitmap {
        val result = Bitmap.createBitmap(photo_drawee_view.width,
            photo_drawee_view.height,
            Bitmap.Config.RGB_565)
        val c = Canvas(result)
        photo_drawee_view.draw(c)
        return result
    }

}