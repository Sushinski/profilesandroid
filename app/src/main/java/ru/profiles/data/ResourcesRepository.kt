package ru.profiles.data

import android.net.Uri
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import ru.profiles.api.interfaces.ResourcesApi
import ru.profiles.dao.AuthModelDao
import ru.profiles.model.pojo.FileUploadResponse
import java.util.concurrent.TimeUnit
import okhttp3.RequestBody
import com.facebook.common.file.FileUtils
import okhttp3.MultipartBody



class ResourcesRepository private constructor(private val mAuthDao: AuthModelDao,
                                              private val mResourcesApi: ResourcesApi) {

    companion object{

        @Volatile private var instance: ResourcesRepository? = null

        fun getInstance(authDao: AuthModelDao, resourcesApi: ResourcesApi) =
            instance ?: synchronized(this) {
                instance ?: ResourcesRepository(authDao, resourcesApi).also { instance = it }
            }
    }

    fun saveImageFile(imageFile: RequestBody ): Single<FileUploadResponse> {
        val imageBody = MultipartBody.Part.createFormData("file", "user_image", imageFile)
        return mAuthDao.getUserAuth().flatMap{ auth->mResourcesApi.uploadFile(auth.mJwtToken, imageBody)}
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .firstOrError()
    }

}