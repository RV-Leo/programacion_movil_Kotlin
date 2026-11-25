# Registro de prompts y decisiones

El usuario pidió mejoras para Fase 2 y un modo oscuro con un diseño minimalista y creativo. La implementación se desarrolló directamente a partir de ese encargo. No se consultó Gemini en esta ejecución; la columna de respuesta describe el análisis aplicado, no una respuesta atribuida a Gemini.

| Prompt | Respuesta o análisis aplicado | Qué se implementó o ajustó | Criterio técnico |
| --- | --- | --- | --- |
| "Agrega eliminar publicación con DELETE y revisa la respuesta." | La API debe devolver `Response<Unit>`; el Repository inspecciona `isSuccessful` y convierte la respuesta no exitosa en fallo de `Result`. | Se agregó `@DELETE`, `PostRepository.eliminar`, estado de eliminación, confirmación, reintento y actualización local al tener éxito. | La capa UI no conoce Retrofit ni el código HTTP; cancelar una corrutina no se transforma en un error de usuario. |
| "Agrega búsqueda por título a las publicaciones cargadas." | Conviene filtrar localmente para no hacer una petición cada vez que se escribe. | `PostsViewModel` mantiene el texto y publica `listaVisible`; la lista presenta coincidencias y un estado vacío. | La búsqueda no cambia el filtro remoto por usuario ni aumenta el tráfico de red. |
| "Agrega un encabezado Accept a todas las peticiones." | Un interceptor de OkHttp aplica el mismo encabezado al tráfico completo, incluidos futuros endpoints. | Se agregó `Accept: application/json` en `RetrofitClient`. | La configuración pertenece al cliente HTTP, no a las pantallas o al ViewModel. |
| "Haz la app minimalista, creativa y con modo oscuro global." | Es mejor usar una paleta controlada en ambos temas que colores dinámicos distintos por dispositivo. | Se creó una paleta verde petróleo/menta con acentos coral y dorado, un conmutador global y tarjetas de publicación con jerarquía y badges. | Mantiene contraste, identidad visual y coherencia entre lista, detalle y formulario. |

## Prompt reutilizable

El prompt completo, con contexto, reglas de arquitectura y criterios de aceptación, está en [PROMTS.md](PROMTS.md).
