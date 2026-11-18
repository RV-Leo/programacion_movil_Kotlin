package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repo: CafeRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> {
                SesionViewModel(repo, prefs) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(repo) as T
            }
            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) -> {
                PedidosClienteViewModel(repo) as T
            }
            modelClass.isAssignableFrom(PanelAdminViewModel::class.java) -> {
                PanelAdminViewModel(repo) as T
            }
            modelClass.isAssignableFrom(PedidosAdminViewModel::class.java) -> {
                PedidosAdminViewModel(repo) as T
            }
            modelClass.isAssignableFrom(MenuAdminViewModel::class.java) -> {
                MenuAdminViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
