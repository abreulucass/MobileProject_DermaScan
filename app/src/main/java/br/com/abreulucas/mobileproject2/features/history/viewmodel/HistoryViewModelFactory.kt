package br.com.abreulucas.mobileproject2.features.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.abreulucas.mobileproject2.database.dao.ConsultaDao

class HistoryViewModelFactory(
    private val consultaDao: ConsultaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(consultaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}