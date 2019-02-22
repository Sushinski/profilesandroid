package ru.profiles.api.builder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import ru.profiles.api.interfaces.ServicesApi
import java.util.concurrent.TimeUnit

class ServicesApiBuilder private constructor() : BaseBuilder() {

    companion object {
        fun buildForUrl(base_url: String, gson_converter: Converter.Factory): ServicesApi {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gson_converter)
                .client(
                    OkHttpClient()
                        .newBuilder()
                        .addInterceptor(interceptor)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build() )
                .build().create()
        }
    }
}