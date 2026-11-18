package com.ronda.campusgps.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronda.campusgps.data.*
import com.ronda.campusgps.model.*
import com.ronda.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class MapaViewModel(private val ubicacionRepo: UbicacionRepository, private val lugaresRepo: LugaresRepository): ViewModel() {
 private val _estado=MutableStateFlow<UbicacionUiState>(UbicacionUiState.Inicial); val estado=_estado.asStateFlow()
 private val _lugares=MutableStateFlow(lugaresRepo.obtenerLugares().map{LugarConDistancia(it,null)}); val lugares=_lugares.asStateFlow()
 private val _seleccionado=MutableStateFlow<Int?>(null); val seleccionado=_seleccionado.asStateFlow()
 fun buscar(){ viewModelScope.launch { _estado.value=UbicacionUiState.Buscando; try { val p=ubicacionRepo.obtenerUbicacion(); if(p==null) _estado.value=UbicacionUiState.Error("No se pudo obtener la ubicación") else { _lugares.value=lugaresRepo.obtenerLugares().map{LugarConDistancia(it,distanciaMetros(p,it))}.sortedBy{it.metros}; _estado.value=UbicacionUiState.Lista(p) } } catch(e:Exception){_estado.value=UbicacionUiState.Error(e.message ?: "No se pudo obtener la ubicación")} } }
 fun permisoDenegado(){_estado.value=UbicacionUiState.Error("Sin permiso de ubicación")}
 fun seleccionar(id:Int?){_seleccionado.value=id}
}
