# ComputerStore 💻

Aplicación móvil Android desarrollada en **Kotlin** y **Jetpack Compose** para la gestión y catálogo de una tienda de equipos de cómputo.

## 🚀 Características y Funcionalidades

- **Autenticación con Firebase Auth & Firestore**:
  - Inicio de sesión por correo y contraseña.
  - Roles de usuario (`USUARIO` y `ADMIN`) consultados en tiempo real desde Firestore.
- **Catálogo de Productos**:
  - Consumo de API REST remota con **Retrofit**.
  - Persistencia local con **Room** como *Single Source of Truth* (Fuente Única de Verdad).
- **Modo Sin Conexión**:
  - Visualización del catálogo guardado localmente cuando no hay acceso a internet.
  - Aviso contextual en la interfaz de usuario.
- **Favoritos**:
  - Marcar/desmarcar productos favoritos y pantalla dedicada para su consulta.
- **Panel de Administración**:
  - Acceso restringido únicamente para usuarios con rol `ADMIN`.
  - Estadísticas de productos y favoritos guardados.
  - Botón de sincronización con la API y limpieza de caché local.
- **Notificaciones Locales**:
  - Notificación de sistema al sincronizar catálogo y al realizar compras.

## 🛠️ Arquitectura y Tecnologías

- **Arquitectura**: MVVM + Clean Architecture + Repository Pattern.
- **UI**: Jetpack Compose, Material Design 3, Navigation Compose.
- **Base de datos local**: Room Database (Entity, DAO, Database).
- **Red & API**: Retrofit + Gson Converter.
- **Backend / Auth**: Firebase Authentication & Cloud Firestore.
- **Asincronía**: Kotlin Coroutines & StateFlow.

## 📋 Requisitos Previos e Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/RV-Leo/programacion_movil_Kotlin.git
   ```
2. Abrir el proyecto en **Android Studio** (versión recomendada 2024.2+).
3. Asegurarse de tener el archivo `google-services.json` configurado dentro de la carpeta `app/`.
4. Compilar y ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.
