package com.ronda.campusgps.ui
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.ronda.campusgps.model.*
@Composable fun MapaUbicacion(estado: UbicacionUiState, lugares: List<LugarConDistancia>, modifier: Modifier=Modifier) {
 val camara=rememberCameraPositionState { position=CameraPosition.fromLatLngZoom(LatLng(-12.0464,-77.0428),12f) }
 LaunchedEffect(estado) { if(estado is UbicacionUiState.Lista) camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(estado.posicion.latitud,estado.posicion.longitud),16f)) }
 GoogleMap(modifier=modifier,cameraPositionState=camara) {
  if(estado is UbicacionUiState.Lista) Marker(state=rememberUpdatedMarkerState(LatLng(estado.posicion.latitud,estado.posicion.longitud)),title="Estás aquí")
  lugares.forEach { item -> val l=item.lugar; key(l.id) { Marker(state=rememberUpdatedMarkerState(LatLng(l.latitud,l.longitud)),title=l.nombre,snippet=l.categoria,icon=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) } }
 }
}
