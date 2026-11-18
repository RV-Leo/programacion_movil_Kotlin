package com.ronda.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ronda.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
) : UbicacionRepository {

    override fun playServicesDisponible(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    @SuppressLint("MissingPermission")
    override suspend fun obtenerUbicacion(): Posicion? {
        return try {
            val location = fusedLocationClient.lastLocation.await()
            if (location != null) {
                Posicion(location.latitude, location.longitude)
            } else {
                Posicion(-12.046374, -77.042793)
            }
        } catch (_: Exception) {
            Posicion(-12.046374, -77.042793)
        }
    }
}
