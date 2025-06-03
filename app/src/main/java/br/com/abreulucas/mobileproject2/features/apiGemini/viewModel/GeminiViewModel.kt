package br.com.abreulucas.mobileproject2.features.apiGemini.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.abreulucas.mobileproject2.features.apiGemini.service.GeminiRetrofitInstance
import br.com.abreulucas.mobileproject2.features.apiGemini.model.Contents
import br.com.abreulucas.mobileproject2.features.apiGemini.model.GeminiRequest
import br.com.abreulucas.mobileproject2.features.apiGemini.model.Parts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GeminiViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableStateFlow<String?>(null)
    val response = _response.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val apiKey = "AIzaSyCxWeIPY65s9c7U4ArHTq4BpcBC0gas1dU"

    fun askGemini(prompt: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = GeminiRequest(
                    contents = listOf(
                        Contents(parts = listOf(Parts(text = prompt)))
                    )
                )

                // Colocar um erro de conexao
                val geminiResponse = GeminiRetrofitInstance.api.generateContent(apiKey, request)

                val output = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Sem resposta do Gemini"

                _response.value = output
            } catch (e: IOException) {
                _response.value = "Erro de rede: ${e.localizedMessage}"
            } catch (e: HttpException) {
                _response.value = "Erro HTTP: ${e.localizedMessage}"
            } catch (e: Exception) {
                _response.value = "Erro inesperado: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}