package com.ronda.campusgps.data
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ronda.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await
class UbicacionRepositoryGoogle(context: Context): UbicacionRepository {
 private val client=LocationServices.getFusedLocationProviderClient(context)
 @Suppress("MissingPermission") override suspend fun obtenerUbicacion(): Posicion? { val l=client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,null).await() ?: client.lastLocation.await(); return l?.let { Posicion(it.latitude,it.longitude) } }
}
