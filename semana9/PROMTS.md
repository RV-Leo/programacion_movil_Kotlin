# Prompt maestro para mejorar MuroCampus

Pega este prompt en Gemini Studio Code con el proyecto `semana9` abierto:

> Actúa como una persona desarrolladora Android con experiencia en Kotlin, Jetpack Compose y arquitectura MVVM + Repository. Revisa el proyecto MuroCampus existente y trabaja solo dentro de `semana9`; conserva sus paquetes `com.ronda.murocampus`, sus pantallas y el flujo funcional actual.
>
> Implementa estas mejoras, separando cada mejora en un cambio revisable:
>
> 1. **Eliminar publicaciones:** agrega `@DELETE("posts/{id}")` en `PostApi` con `@Path("id")` y `Response<Unit>`. El Repository debe verificar `isSuccessful`, convertir los fallos en `Result.failure` y volver a lanzar `CancellationException`. El ViewModel expone un estado observable, elimina localmente el elemento solo si la respuesta fue exitosa y nunca importa Retrofit ni `HttpException`. En el detalle agrega una acción de eliminar con confirmación y estado de error/reintento.
> 2. **Buscar por título:** filtra en memoria las publicaciones que ya están cargadas. El texto de búsqueda debe vivir en el ViewModel y la lista se actualiza al escribir, sin llamadas adicionales al servidor.
> 3. **Encabezado HTTP:** configura un interceptor de OkHttp que aplique `Accept: application/json` a todas las solicitudes.
> 4. **Modo oscuro y diseño:** agrega un control persistente para cambiar entre tema claro y oscuro en toda la aplicación. Usa una paleta editorial minimalista de verde petróleo, menta, coral y dorado; fondo despejado, jerarquía tipográfica clara, tarjetas redondeadas, badges discretos y controles accesibles. Asegura contraste suficiente en ambos temas y que lista, detalle, formulario, chips, errores y diálogos usen `MaterialTheme.colorScheme`.
>
> Reglas: los Composables no llaman a Retrofit; el ViewModel conoce solo interfaces de Repository y estados de UI; el Repository traduce excepciones a `Result`; relanza cancelaciones. No cambies archivos de otras semanas. Mantén nombres claros, valida que la app compile y resume los archivos y decisiones técnicas al terminar.

## Criterios de aceptación

- Eliminar pide confirmación, procesa la respuesta en Repository y solo quita la publicación de la lista tras éxito.
- Buscar coincide con el título sin distinguir mayúsculas y minúsculas, y no hace peticiones de red.
- El encabezado aparece en solicitudes GET, POST y DELETE.
- El cambio de tema afecta todas las pantallas y se conserva al recrear la actividad.
- `PostsViewModel` no conoce Retrofit ni `HttpException`; las pantallas no conocen `PostApi`.
