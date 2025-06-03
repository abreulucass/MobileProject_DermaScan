package br.com.abreulucas.mobileproject2.features.apiRoboflow.service

import br.com.abreulucas.mobileproject2.features.apiRoboflow.model.RoboflowRequest
import br.com.abreulucas.mobileproject2.features.apiRoboflow.model.RoboflowResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RoboflowService {
    @Headers("Content-Type: application/json")
    @POST("infer/workflows/healthproject/detect-and-classify")
    suspend fun classifyImage(@Body request: RoboflowRequest): RoboflowResponse
}