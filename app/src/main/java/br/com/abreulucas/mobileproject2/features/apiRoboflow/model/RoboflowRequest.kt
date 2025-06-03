package br.com.abreulucas.mobileproject2.features.apiRoboflow.model

import br.com.abreulucas.mobileproject2.features.images.model.image

data class RoboflowRequest(
    val api_key: String,
    val inputs: Map<String, image>
)
