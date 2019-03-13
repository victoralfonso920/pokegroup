package com.victordev.pokegroup.LibModule

import android.app.Activity
import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.victordev.pokegroup.BuildConfig
import com.victordev.pokegroup.utils.Utils
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class LibModule(internal var mBaseUrl: String, internal var mActivity: Context, var app: Activity) {

    //Gson
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }


    //cliente okhttp
    @Provides
    @Singleton
    internal fun provideOkhttpClient(): OkHttpClient {
        try {
            var loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.addInterceptor(loggingInterceptor)
            builder.connectTimeout(300,TimeUnit.SECONDS)
            builder.readTimeout(80,TimeUnit.SECONDS)
            builder.writeTimeout(90,TimeUnit.SECONDS)
            builder.retryOnConnectionFailure(true)
            builder.addNetworkInterceptor(Interceptor {
                val request: Request = it.request().newBuilder().addHeader("Connection", "close").build()
                return@Interceptor it.proceed(request)
            })
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(OkHttpProfilerInterceptor())
            }
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    //retrofit
    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build()
    }
    //globales
    @Provides
    @Singleton
    internal fun provideGlobales(): Utils {
        var funciones: Utils? = null
        funciones = Utils()
        return funciones
    }



}