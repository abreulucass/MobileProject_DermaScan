package br.com.abreulucas.mobileproject2.features.apiRoboflow.model

data class ClassificationPrediction(
    val inference_id: String?,
    val predictions: ClassificationDetails?
)