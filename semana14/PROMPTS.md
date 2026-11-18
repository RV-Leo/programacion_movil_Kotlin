# Prompts de IA

No se usó Gemini en esta sesión. Para no atribuirle respuestas que no dio, la columna solicitada registra explícitamente las sugerencias de Codex.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Mueve la tarea con progreso al Repository y expón el avance con Flow." | Gemini no se usó. Codex sugirió que el Repository emita el porcentaje y use `flowOn(Dispatchers.Default)`. | La implementación usa 100 emisiones y mantiene la cancelación cooperativa. | La tarea simulada queda fuera del ViewModel y no bloquea la UI. |
| "Agrega un tiempo máximo de 2 segundos en las cargas." | Gemini no se usó. Codex propuso envolver ambas variantes en `withTimeout(2_000)`. | Se atrapa `TimeoutCancellationException` antes de `CancellationException`. | Así el timeout se muestra como estado de error y la cancelación del usuario sigue propagándose. |
| "Reintenta una carga fallida hasta tres veces con espera creciente." | Gemini no se usó. Codex propuso esperas de 500, 1000 y 2000 ms. | Se conservó ese intervalo y se informa el último error. | Evita reintentos inmediatos y mantiene un límite claro. |
| "Muestra el progreso mientras se cuentan primos." | Gemini no se usó. Codex propuso reportar porcentajes desde un Flow del Repository. | El Flow emite también la cantidad final para evitar recalcular los primos. | El ViewModel actualiza porcentaje y resultado desde la misma operación cancelable. |
