package com.ronda.campusgps.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ronda.campusgps.data.*
class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = MapaViewModel(UbicacionRepositoryGoogle(context), LugaresRepositoryLocal()) as T
}
