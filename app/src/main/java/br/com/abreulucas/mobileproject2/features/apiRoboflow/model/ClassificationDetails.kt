package br.com.abreulucas.mobileproject2.features.apiRoboflow.model

data class ClassificationDetails(
    val inference_id: String?,
    val predictions: List<ClassificationResult>?,
    val top: String?,
    val confidence: Double?,
    val prediction_type: String?,
)

