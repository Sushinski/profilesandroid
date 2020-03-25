package ru.profiles.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.interfaces.UserRepositoryOps
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ImageEditViewModel @Inject constructor(private val mUserRep: UserRepositoryOps) : ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun saveCroppedFile(ctx: Context, croppedImage: Bitmap): Uri? {
        val saveUri =  createPicFile(ctx)
        try {
            val outputStream = saveUri?.let { ctx.contentResolver?.openOutputStream(it) }
            outputStream?.let{
                croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }.also {
                outputStream?.close()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        croppedImage.recycle()
        return saveUri
    }

    private fun createPicFile(ctx: Context): Uri? {
        // Create an image file name
        val storageDir: File? = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val f =  storageDir?.let{
            try {
                File.createTempFile(
                    "img", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                )
            }catch (e: IOException){
                null
            }
        }
        return f?.let{
            FileProvider.getUriForFile(
                ctx,
                "ru.profiles.profiles.fileprovider",
                it
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        //viewModelJob.cancel()
    }

}