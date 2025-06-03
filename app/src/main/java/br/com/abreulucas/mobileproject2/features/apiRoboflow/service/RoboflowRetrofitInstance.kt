package br.com.abreulucas.mobileproject2.features.apiRoboflow.service

import br.com.abreulucas.mobileproject2.features.apiRoboflow.service.RoboflowService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RoboflowRetrofitInstance {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: RoboflowService by lazy {
        Retrofit.Builder()
            .baseUrl("https://detect.roboflow.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RoboflowService::class.java)
    }
}