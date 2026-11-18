# PROMPTS.md

Registro de asistencia de IA para esta implementación. No se ingresó ninguna clave de API.

| Prompt | Respuesta resumida | Cambios aplicados o descartados | Criterio |
|---|---|---|---|
| “Implementa el laboratorio CampusGPS de semana 13 desde el PDF y el proyecto base, siguiendo MVVM + Repository.” | Se implementaron permisos, ubicación, mapa, marcadores, lista ordenada, selección, lugar cercano y errores tipados. | Se adaptaron paquetes y contratos al proyecto existente `com.ronda.campusgps`; la distancia queda fuera de Compose. | Mantener separación de capas, datos ficticios y no versionar `local.properties`. |
