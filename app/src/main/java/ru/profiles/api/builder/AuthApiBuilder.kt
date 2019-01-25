package ru.profiles.api.builder


import android.util.Log
import okhttp3.Interceptor
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import ru.profiles.api.interfaces.AuthApi
import java.io.IOException


class AuthApiBuilder private constructor() : BaseBuilder() {

    companion object {
        fun buildForUrl(base_url: String, gson_converter: Converter.Factory): AuthApi{
            return Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gson_converter)
                .client(OkHttpClient().newBuilder().addInterceptor(LoggingInterceptor("AuthRequest")).build() )
                .build().create()
        }
    }

}