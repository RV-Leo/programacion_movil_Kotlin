# 🚀 Prompt Maestro para la Construcción de ComputerStore

Este archivo contiene el **Prompt Maestro** diseñado para ser utilizado por cualquier desarrollador o agente de Inteligencia Artificial para generar, replicar y extender el diseño, arquitectura y funcionalidades avanzadas de la aplicación móvil **ComputerStore**.

---

## 🎯 Master Prompt / Prompt de Arquitectura e Implementación

> **Instrucciones para el Agente IA:**
> Copia y utiliza el siguiente prompt completo para generar o refactorizar la aplicación Android siguiendo las mejores prácticas de desarrollo.

```markdown
Actúa como un Desarrollador Senior de Android y experto en Jetpack Compose. Tu objetivo es diseñar e implementar la arquitectura completa y la interfaz de usuario de una aplicación móvil de comercio electrónico de componentes tecnológicos llamada "ComputerStore".

### 🛠️ 1. Arquitectura y Estructura del Proyecto
- Sigue el patrón MVVM + Clean Architecture + Repository Pattern + Single Source of Truth con Room Database (KSP).
- Organiza el paquete principal (`com.tecsup.computerstore`) en la siguiente estructura modular:
  - `data/remote/`: `ApiService` (Retrofit HTTP), `RetrofitClient`, DTOs (`ItemDto`, `ProductResponseDto`).
  - `data/local/`: `ItemEntity` (@Entity), `ItemDao` (@Dao), `AppDatabase` (@Database KSP/Room).
  - `data/auth/`: `AuthRepository` (Interface) y `FirebaseAuthRepository` (Firebase Auth + Cloud Firestore).
  - `data/repository/`: `ItemRepositoryImpl` (Sincronización remota e integración con Room como fuente única de verdad).
  - `data/mapper/`: `Mappers.kt` (Conversión limpia DTO ➔ Entity ➔ Dominio).
  - `domain/model/`: `Item.kt` (Modelo de producto puro) y `Sesion.kt` (Estado del usuario y Enum `Rol`: `USUARIO` / `ADMIN`).
  - `notification/`: `Notificador` (Interface) y `NotificadorAndroid` (Notificaciones del sistema con `NotificationCompat`).
  - `ui/navigation/`: `AppNav.kt` (Módulo central de navegación con `NavHost`).
  - `ui/theme/`: `Color.kt`, `Theme.kt`, `Type.kt` (Sistema de diseño M3).
  - `ui/`: Pantallas y ViewModels independientes (`login`, `registro`, `lista`, `detalle`, `favoritos`, `admin`, `carrito`, `perfil`, `procesos`).

### 🎨 2. Sistema de Diseño e Interfaz (Jetpack Compose & Material Design 3)
- **Pantalla Completa Edge-to-Edge**: Todas las pantallas deben ocupar el 100% de la superficie del dispositivo (sin contenedores o tarjetas flotantes reducidas en el centro).
- **Modo Oscuro (Dark Theme)**: Fondo gris oscuro realista (`#121214`), tarjetas y superficies elevadas (`#1E1E24`), variantes (`#2B2B36`), textos en blanco/gris claro (`#F3F4F6`), y acentos en índigo vibrante (`#818CF8`).
- **Modo Claro (Light Theme)**: Fondo blanco puro (`#FFFFFF`), texto oscuro de alto contraste (`#111827`), y acentos en índigo profundo (`#4F46E5`).
- **Iconografía e Indicadores**:
  - Los botones e iconos de favoritos deben usar un tono rojo puro (`#EF4444`).
  - Las notificaciones flotantes (Snackbars) al marcar un producto como favorito deben incluir un icono nativo de Compose (`Icons.Default.Favorite` / `Icons.Default.FavoriteBorder`) estilizado con color rojo, en lugar de emojis de texto.

### 🌐 3. Sincronización de Datos, Modo Offline y Pruebas
- **Consumo de API y Persistencia**: `ItemRepositoryImpl` debe descargar los datos desde Retrofit, mapearlos a Room y exponerlos hacia la UI a través de `Flow<List<Item>>`.
- **Modo Sin Conexión**: Si la red falla o no hay conexión a internet, la app debe continuar funcionando cargando los datos guardados en SQLite/Room y mostrar una alerta contextual reintentable.
- **Pruebas Unitarias de ViewModel**: Configura `kotlinx-coroutines-test` y crea pruebas unitarias para `ListaViewModelTest` verificando la emisión de estados `UiState.Exito` y la lógica reactiva de favoritos.

### 🔐 4. Autenticación, Roles y Navegación
- Maneja pantallas separadas e independientes para Inicio de Sesión (`LoginScreen`) y Registro de Cuentas (`RegistroScreen`).
- Implementa roles de usuario mediante Cloud Firestore:
  - `Rol.USUARIO`: Acceso a catálogo, carrito, favoritos y perfil.
  - `Rol.ADMIN`: Acceso adicional al panel de administración (`AdminScreen`) y gestión de ventas/procesos.
```

---

## 🧪 Instrucciones de Prueba y Verificación

1. **Pruebas de Red y Sincronización**:
   - Ejecutar la tarea Gradle de pruebas unitarias:
     ```bash
     ./gradlew :app:testDebugUnitTest
     ```
2. **Compilación e Instalación**:
   - Compilar el APK de depuración:
     ```bash
     ./gradlew :app:assembleDebug
     ```
