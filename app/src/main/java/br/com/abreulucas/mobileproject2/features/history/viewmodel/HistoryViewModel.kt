package br.com.abreulucas.mobileproject2.features.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.abreulucas.mobileproject2.database.dao.ConsultaDao
import br.com.abreulucas.mobileproject2.database.entity.Consulta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val consultaDao: ConsultaDao) : ViewModel() {

    private val _consultas = MutableStateFlow<List<Consulta>>(emptyList())
    val consultas: StateFlow<List<Consulta>> = _consultas

    init {
        carregarConsultas()
    }

    fun carregarConsultas() {
        viewModelScope.launch {
            val lista = consultaDao.getAllConsultas()
            _consultas.value = lista
        }
    }
}