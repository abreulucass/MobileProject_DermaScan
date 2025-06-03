package br.com.abreulucas.mobileproject2.features.apiRoboflow.model

import com.google.gson.annotations.SerializedName

data class ClassificationResult(
    @SerializedName("class") val className: String,
    val class_id: Int,
    val confidence: Double
)
