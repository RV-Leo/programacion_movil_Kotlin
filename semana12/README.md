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

<img width="407" height="897" alt="image" src="https://github.com/user-attachments/assets/502ebb4d-a329-4468-b31e-5103828c7d6b" />

<img width="386" height="870" alt="image" src="https://github.com/user-attachments/assets/460455ec-9a3f-4079-aca7-6aab89f68f73" />

<img width="432" height="781" alt="image" src="https://github.com/user-attachments/assets/0e521b8f-5cba-425d-a1ba-393338caf96a" />

<img width="408" height="889" alt="image" src="https://github.com/user-attachments/assets/ef097208-b1c0-40dd-90a4-4b96038c0554" />

<img width="399" height="835" alt="image" src="https://github.com/user-attachments/assets/a3244dbe-47dc-48fb-9946-c9778102bfda" />



## Imágenes locales de productos

Guarda cada imagen en `app/src/main/res/drawable/` con el formato `product_ID.webp`, donde `ID` es el identificador del producto. Por ejemplo, el producto local `10_001` usa `product_10001.webp`. También se aceptan PNG y JPG. La pantalla busca primero el drawable local; si no existe, usa la imagen remota o el marcador de categoría.

## 🛠️ Arquitectura y Tecnologías

- **Arquitectura**: MVVM + Clean Architecture + Repository Pattern.
- **UI**: Jetpack Compose, Material Design 3, Navigation Compose.
- **Base de datos local**: Room Database (Entity, DAO, Database).
- **Red & API**: Retrofit + Gson Converter.
- **Autenticación y perfiles**: Firebase Authentication y Cloud Firestore para cuentas, perfiles y roles. Los productos se guardan localmente con Room; Firebase no es el almacén del catálogo.
- **Asincronía**: Kotlin Coroutines & StateFlow.

## Accesos de la app

La pantalla inicial presenta un switch para elegir **Cliente** o **Administrador**. Si no existe una sesión activa, la app muestra el Login; una sesión válida entra al catálogo.

- Administrador de demostración: `admin@admin.com`
- Contraseña: `admin123456`

En Firebase Console, habilita el proveedor **Authentication → Sign-in method → Email/Password** y publica [`firestore.rules`](firestore.rules) en **Firestore → Rules**. Después de configurar `google-services.json`, el primer inicio de sesión con esos datos crea la cuenta y el perfil `admin`. El registro normal crea usuarios con rol `usuario` y bloquea el correo reservado del administrador. Estas credenciales son para desarrollo/demo; cámbialas antes de publicar o usar con información real.

El cliente ve Inicio, Carrito y Perfil en la navegación inferior. El administrador ve Inicio, Procesos y Perfil; solo su rol permite consultar y actualizar todos los pedidos. Los pedidos e importes siguen siendo demostrativos y no sustituyen un backend de pagos.

## 📋 Requisitos Previos e Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/RV-Leo/programacion_movil_Kotlin.git
   ```
2. Abrir el proyecto en **Android Studio** (versión recomendada 2024.2+).
3. Asegurarse de tener el archivo `google-services.json` configurado dentro de la carpeta `app/`.
4. Compilar y ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.
