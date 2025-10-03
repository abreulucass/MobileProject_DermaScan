package br.com.abreulucas.mobileproject2.features.apiRoboflow.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.abreulucas.mobileproject2.features.apiRoboflow.model.RoboflowRequest
import br.com.abreulucas.mobileproject2.features.apiRoboflow.service.RoboflowRetrofitInstance
import br.com.abreulucas.mobileproject2.features.images.model.image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Base64

class RoboflowViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _result = MutableStateFlow<String?>(null)
    val result = _result.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun classifyImage(context: Context, uri: Uri) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val imageBytes = inputStream?.readBytes() ?: return@launch
                val base64 = Base64.getEncoder().encodeToString(imageBytes)

                val request = RoboflowRequest(
                    api_key = "",
                    inputs = mapOf("image" to image(type = "base64", value = base64))
                )

                //Colocar uma mensagem de erro caso a resposta seja nula

                val response = RoboflowRetrofitInstance.api.classifyImage(request)

                val firstOutput = response.outputs?.firstOrNull()

                val classification = firstOutput?.classification_predictions?.firstOrNull()

                val prediction = classification?.predictions

                val result = prediction?.predictions?.firstOrNull()

                _result.value = result?.let {
                    "${it.className} com ${(it.confidence * 100)}% de certeza"
                } ?: "Nenhuma previsão encontrada"
            } catch (e: Exception) {
                _result.value = "Erro: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
