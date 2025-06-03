package br.com.abreulucas.mobileproject2.features.apiRoboflow.model

data class RoboflowResponse(
    val outputs: List<RoboflowOutput>?,
    val profiler_trace: List<Any>?
)