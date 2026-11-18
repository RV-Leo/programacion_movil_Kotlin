# PROMPTS.md

## Consultas preparadas para la fase con IA

Estas consultas son plantillas para usar durante la mejora de CampusGPS. No representan respuestas que ya se hayan recibido. Después de consultar la IA, registra la respuesta real y tu evaluación. No incluyas claves de API ni credenciales en las consultas.

### 1. Filtrar lugares por categoría

> Analiza la arquitectura actual de CampusGPS en Kotlin, Jetpack Compose y MVVM + Repository. Quiero agregar filtros por categoría (Estudio, Comida, Eventos y Servicios) que actualicen tanto los marcadores como la lista. Primero identifica los archivos y flujos de estado que habría que modificar; luego propone una implementación pequeña que respete las interfaces existentes. La lógica del filtro debe quedar en el ViewModel o en una función pura, y los Composables solo deben representar el estado. No agregues Context al ViewModel ni cambies las coordenadas ficticias. Incluye los casos sin filtro y sin resultados.

### 2. Radio de cercanía

> Revisa el cálculo de distancias y el mapa de CampusGPS. Propón cómo permitir elegir un radio de 100, 200 o 500 metros alrededor de la ubicación actual, mostrar ese radio en el mapa y señalar cuáles lugares están dentro. Mantén el cálculo y la selección de lugares fuera de los Composables, conserva MVVM + Repository y explica qué estado nuevo necesita el ViewModel. Considera qué debe mostrar la app antes de obtener ubicación y cuando el permiso no está concedido. Presenta los cambios por archivo y evita modificar archivos que no sean necesarios.

### 3. Seguimiento de ubicación en vivo

> Diseña una mejora para iniciar y detener el seguimiento de ubicación en CampusGPS. Usa FusedLocationProviderClient dentro de UbicacionRepositoryGoogle y expón las actualizaciones como Flow mediante callbackFlow, con cancelación correcta del registro al detenerse o cerrarse la colección. El ViewModel debe controlar el estado de seguimiento sin recibir Context; la pantalla solo debe mostrar el estado y enviar eventos. Indica cómo manejar permisos revocados y errores del proveedor, y qué pruebas manuales comprobarían el ciclo iniciar/detener.

## Registro de resultados

Completa esta tabla después de usar cada consulta, con la respuesta recibida y los cambios que realmente hayas aceptado o descartado.

| Consulta | Resumen de la respuesta recibida | Cambios aplicados o descartados | Motivo técnico |
|---|---|---|---|
| Filtro por categoría | Pendiente de consulta | Pendiente | Pendiente |
| Radio de cercanía | Pendiente de consulta | Pendiente | Pendiente |
| Seguimiento en vivo | Pendiente de consulta | Pendiente | Pendiente |
