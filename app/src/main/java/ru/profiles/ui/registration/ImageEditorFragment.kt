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
import com.albinmathew.photocrop.cropoverlay.edge.Edge
import com.albinmathew.photocrop.cropoverlay.utils.ImageViewUtil
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.image_editor_fragment.*
import kotlinx.android.synthetic.main.image_editor_fragment.view.*
import ru.profiles.profiles.R
import java.io.File
import java.io.IOException


class ImageEditorFragment: DaggerFragment() {

    private var mSaveUri: Uri? = null
    private val mOutputFormat = Bitmap.CompressFormat.JPEG

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.image_editor_fragment, container, false)

        val a = (activity as AppCompatActivity)
        a.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        a.supportActionBar?.hide()
        v.done_fab.setOnClickListener { saveOutput(a) }
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

    private fun getCurrentDisplayedImage(): Bitmap {
        val result = Bitmap.createBitmap(photo_drawee_view.width,
            photo_drawee_view.height,
            Bitmap.Config.RGB_565)
        val c = Canvas(result)
        photo_drawee_view.draw(c)
        return result
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

    private fun saveOutput(ctx: Context): Boolean {
        val croppedImage = getCroppedImage()
        try {
            val photoFile: File? = try {
                createPicFile(ctx)
            } catch (ex: IOException) {
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also { file ->
                mSaveUri = FileProvider.getUriForFile(
                    ctx,
                    "ru.profiles.profiles.fileprovider",
                    file
                )
                val outputStream = mSaveUri?.let { ctx.contentResolver?.openOutputStream(it) }
                outputStream?.let{
                    croppedImage.compress(mOutputFormat, 90, outputStream)
                }.also {
                    outputStream?.close()
                }
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
            return false
        }
        croppedImage.recycle()
        return true

    }

    @Throws(IOException::class)
    private fun createPicFile(ctx: Context): File? {
        // Create an image file name
        val storageDir: File? = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return storageDir?.let{
            File.createTempFile(
                "img", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
        }
    }

}