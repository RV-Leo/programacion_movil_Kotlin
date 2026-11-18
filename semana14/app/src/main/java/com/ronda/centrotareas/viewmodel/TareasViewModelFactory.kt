package com.ronda.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ronda.centrotareas.data.TareasRepository

class TareasViewModelFactory(private val repository: TareasRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
        }
        return TareasViewModel(repository) as T
    }
}
