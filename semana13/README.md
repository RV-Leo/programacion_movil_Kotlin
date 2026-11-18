# CampusGPS

App Android en Kotlin, Jetpack Compose y Material 3. Obtiene la ubicación del dispositivo, muestra cinco lugares ficticios del campus sobre Google Maps, calcula sus distancias y permite navegar al más cercano.

## Configuración

1. Abre `semana13` en Android Studio y sincroniza Gradle.
2. Activa Maps SDK for Android en Google Cloud y crea una API key restringida al applicationId y a la firma de tu app.
3. Guarda `MAPS_API_KEY=tu_clave` en `local.properties` (no la subas a Git).
4. Ejecuta en un emulador con Google Play y coloca la ubicación cerca de `-12.0464, -77.0428`.

## Paquetes

```text
com.ronda.campusgps/
├── MainActivity.kt
├── data/       # Repositorios de ubicación y lugares
├── model/      # Posición, lugares y estado de pantalla
├── ui/         # Mapa, lista y pantalla Compose
├── util/       # Cálculo y formato de distancias
└── viewmodel/  # Estado y lógica de presentación
```

## MVVM

```text
Pantalla Compose -> MapaViewModel -> Interfaces Repository
                                      |               |
                           UbicacionRepositoryGoogle  LugaresRepositoryLocal
```

La vista solo representa estado y envía eventos. El ViewModel no recibe Context y calcula las distancias; el acceso a Google Play Services vive en el repositorio.

## Lugares ficticios

Biblioteca Central, Cafetería Campus, Laboratorio de Cómputo, Auditorio Principal y Estacionamiento Norte. Coordenadas entregadas por la guía del laboratorio.

## Para pensar

1. `FusedLocationProviderClient` va en el Repository para encapsular el servicio externo. Así el ViewModel depende de una interfaz y se puede probar sin Android Context.
2. El permiso autoriza acceso a la ubicación del usuario; la API key autoriza el uso de Maps SDK. Sin permiso no hay posición personal; sin una key válida el mapa no carga correctamente.
3. `local.properties` evita guardar la clave en el código/versionado. Si se publica, hay que revocarla y crear otra restringida.
4. La distancia se calcula en el ViewModel mediante utilidades puras/de ubicación, no en un Composable, para respetar MVVM y separar presentación de lógica.
5. Si el usuario niega el permiso, la pantalla muestra el error y ofrece abrir ajustes de la app. No se intenta consultar ubicación hasta que el permiso vuelva a estar concedido.


