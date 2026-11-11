# ComputerStore 💻

Aplicación móvil Android desarrollada en **Kotlin** y **Jetpack Compose** para la gestión y catálogo de una tienda de equipos de cómputo.

## 🚀 Características y Funcionalidades

- **Autenticación con Firebase Auth & Firestore**:
  - Inicio de sesión por correo y contraseña.
  - Roles de usuario (`USUARIO` y `ADMIN`) consultados en tiempo real desde Firestore.
- **Catálogo de productos**:
  - Descarga de laptops, tablets y accesorios compatibles desde DummyJSON con **Retrofit**.
  - Catálogo de muestra local para placas madre, procesadores, memoria RAM, almacenamiento, tarjetas gráficas, teclados, mouse, hubs USB, monitores portátiles, micrófonos, webcams y audífonos.
  - **Room** conserva el catálogo y los favoritos para consultarlos sin conexión. Los precios y stocks del catálogo de muestra son demostrativos.
- **Modo Sin Conexión**:
  - Visualización del catálogo guardado localmente cuando no hay acceso a internet.
  - Aviso contextual en la interfaz de usuario.
- **Favoritos**:
  - Marcar/desmarcar productos desde el catálogo y el detalle, con pantalla dedicada.
- **Carrito y pedidos**:
  - Selector de cantidades, validación de stock y cálculo del subtotal y total.
  - Registro de pedidos de demostración en Firestore y consulta del historial por cliente.
  - No procesa pagos reales; el total del panel es una suma demostrativa.
- **Panel de Administración**:
  - Acceso restringido únicamente para usuarios con rol `ADMIN`.
  - Estadísticas de productos y favoritos guardados.
  - Botón de sincronización con la API y limpieza de caché local.
  - La opción inferior **Procesos** muestra pedidos, total demostrativo y permite actualizar su estado.
- **Apariencia**: selector persistente de modo oscuro en el perfil.
- **Navegación inferior**: Inicio, Procesos y Perfil. El carrito se abre desde el encabezado de Inicio.
- **Notificaciones Locales**:
  - Notificación de sistema al sincronizar el catálogo.


## Imágenes locales de productos

Guarda cada imagen en `app/src/main/res/drawable/` con el formato `product_ID.webp`, donde `ID` es el identificador del producto. Por ejemplo, el producto local `10_001` usa `product_10001.webp`. También se aceptan PNG y JPG. La pantalla busca primero el drawable local; si no existe, usa la imagen remota o el marcador de categoría.

## 🛠️ Arquitectura y Tecnologías

- **Arquitectura**: MVVM + Clean Architecture + Repository Pattern.
- **UI**: Jetpack Compose, Material Design 3, Navigation Compose.
- **Base de datos local**: Room Database (Entity, DAO, Database).
- **Red & API**: Retrofit + Gson Converter.
- **Autenticación y perfiles**: Firebase Authentication y Cloud Firestore para cuentas, perfiles y roles. Los productos se guardan localmente con Room; Firebase no es el almacén del catálogo.
- **Asincronía**: Kotlin Coroutines & StateFlow.

## Configurar una cuenta de administrador

La app no contiene usuarios ni contraseñas de administrador predefinidos. Para crear la primera cuenta:

1. En Firebase Console, abre **Authentication → Users → Add user** y registra el correo y la contraseña del administrador.
2. Copia el UID de esa cuenta.
3. En **Firestore Database**, crea la colección `usuarios` y un documento cuyo ID sea ese UID. Guarda los campos `nombre` (texto) y `rol` (texto con valor exacto `admin`).
4. Publica las reglas de [`firestore.rules`](firestore.rules) en la pestaña **Rules** de Firestore.
5. Inicia sesión con esa cuenta en la app. El panel de gestión está en Perfil y el control de pedidos está en la pestaña Procesos.

Las nuevas cuentas creadas desde la app siempre reciben el rol `usuario`; no existe una opción para que un cliente se eleve a administrador. Revisa las reglas de Firestore antes de publicar la app. Los pedidos y sus importes son datos de demostración y no sustituyen un backend de pagos.

## 📋 Requisitos Previos e Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/RV-Leo/programacion_movil_Kotlin.git
   ```
2. Abrir el proyecto en **Android Studio** (versión recomendada 2024.2+).
3. Asegurarse de tener el archivo `google-services.json` configurado dentro de la carpeta `app/`.
4. Compilar y ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.
