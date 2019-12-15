package com.itis2019.websocketapp.api

import com.itis2019.websocketapp.BuildConfig
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TechnoChatApiService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Single<TechnoResponse>

    companion object Factory {

        lateinit var INSTANCE: TechnoChatApiService

        fun create(): TechnoChatApiService {
            synchronized(this) {
                if (!::INSTANCE.isInitialized)
                    INSTANCE = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BuildConfig.API_BASE_URL)
                        .build()
                        .create(TechnoChatApiService::class.java)
                return INSTANCE
            }
        }
    }
}
