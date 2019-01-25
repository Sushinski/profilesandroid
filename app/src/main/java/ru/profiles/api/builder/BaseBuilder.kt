package ru.profiles.api.builder

import android.util.Log
import okhttp3.Interceptor
import java.io.IOException

abstract class BaseBuilder(){

    protected class LoggingInterceptor(private val mTag: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            Log.i(mTag, request.toString())
            return chain.proceed(request)
        }
    }
}