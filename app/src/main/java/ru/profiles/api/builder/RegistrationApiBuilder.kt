package ru.profiles.api.builder

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import ru.profiles.api.interfaces.RegistrationApi

class RegistrationApiBuilder private constructor() : BaseBuilder() {

    companion object {
        fun buildForUrl(base_url: String, gson_converter: Converter.Factory): RegistrationApi {
            return Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gson_converter)
                .client(OkHttpClient().newBuilder().addInterceptor(LoggingInterceptor("RegRequest")).build() )
                .build().create()
        }
    }
}