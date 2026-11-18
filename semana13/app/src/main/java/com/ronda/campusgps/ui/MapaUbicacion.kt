package com.ronda.campusgps.ui
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.ronda.campusgps.model.*
import kotlinx.coroutines.launch
@Composable fun MapaUbicacion(estado: UbicacionUiState, lugares: List<LugarConDistancia>, seleccionado: Int?, masCercano: Lugar?, modifier: Modifier=Modifier) {
 val camera= rememberCameraPositionState { position=CameraPosition.fromLatLngZoom(LatLng(-12.0464,-77.0428),12f) }
 val scope= rememberCoroutineScope()
 LaunchedEffect(estado) { if(estado is UbicacionUiState.Lista) camera.animate(CameraUpdateFactory.newLatLngZoom(LatLng(estado.posicion.latitud,estado.posicion.longitud),16f)) }
 LaunchedEffect(seleccionado,lugares) { val l=lugares.firstOrNull { it.lugar.id==seleccionado }?.lugar; if(l!=null) camera.animate(CameraUpdateFactory.newLatLngZoom(LatLng(l.latitud,l.longitud),17f)) }
 GoogleMap(modifier=modifier, cameraPositionState=camera) {
  if(estado is UbicacionUiState.Lista) Marker(state=rememberUpdatedMarkerState(LatLng(estado.posicion.latitud,estado.posicion.longitud)), title="Estás aquí")
  lugares.forEach { item -> val l=item.lugar; key(l.id) { Marker(state=rememberUpdatedMarkerState(LatLng(l.latitud,l.longitud)), title=l.nombre, snippet=l.categoria, icon=BitmapDescriptorFactory.defaultMarker(if(l.id==masCercano?.id) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_AZURE), onClick={ scope.launch { camera.animate(CameraUpdateFactory.newLatLngZoom(LatLng(l.latitud,l.longitud),17f)) }; true }) } }
 }
}
