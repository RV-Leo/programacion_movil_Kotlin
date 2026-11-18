# CentroTareas

Aplicación Android para practicar corrutinas, `StateFlow`, cancelación y tareas paralelas. Está construida con Kotlin, Jetpack Compose, Material 3 y arquitectura MVVM + Repository. Usa solamente datos ficticios.

## Paquetes

```text
com.ronda.centrotareas/
├── MainActivity.kt
├── model/Estados.kt
├── data/TareasRepository.kt
├── data/TareasRepositoryLocal.kt
├── viewmodel/TareasViewModel.kt
├── viewmodel/TareasViewModelFactory.kt
└── ui/PantallaTareas.kt
```

## Diagrama MVVM

```text
PantallaTareas (Compose) ⇄ TareasViewModel ⇄ TareasRepository (interface)
                                                   ↑
                                        TareasRepositoryLocal
```

La UI observa `StateFlow` con `collectAsState()` y envía eventos al ViewModel. Este solo depende de la interfaz del Repository. El cálculo de primos ocurre en `Dispatchers.Default`; las cargas simuladas usan `delay`.

## Observación del Paso 1

La pantalla se congela 4 segundos porque `Thread.sleep` bloquea el hilo principal, que dibuja la UI y atiende los toques. Aunque el mensaje cambia a «Trabajando...», no llega a pintarse hasta que termina la espera. Una espera de 10 segundos podría provocar el diálogo ANR («La aplicación no responde»). `Thread.sleep` solo estuvo en el commit didáctico del Paso 1 y no aparece en el código final.

## Capturas

![Tarea congelada](capturas/congelada.png) TODO

![Tarea con progreso](capturas/progreso.png) TODO

![Tarea cancelada](capturas/cancelada.png) TODO

![Conteo de primos](capturas/primos.png) TODO

![Cargas y error simulado](capturas/cargas-error.png) TODO

## Para pensar

1. El hilo principal estaba bloqueado; por eso no podía dibujar «Trabajando...» ni atender el contador.
2. `Thread.sleep` bloquea un hilo. `delay` suspende la corrutina sin bloquearlo y coopera con la cancelación.
3. `Dispatchers.Default` sirve para cálculos intensivos de CPU, como primos. `Dispatchers.IO` se usa para operaciones bloqueantes de red o disco.
4. `viewModelScope` se cancela cuando se destruye el ViewModel; ante una rotación, el ViewModel normalmente sobrevive y la tarea continúa.
5. `async` inicia ambas cargas concurrentemente y el tiempo se acerca a la más lenta (3 segundos), no a su suma (5 segundos). `coroutineScope` cancela la tarea hermana y propaga el fallo.

## Mejoras de Fase 2 (`mejora-ia`)

- La tarea con barra de progreso ahora se produce desde un `Flow` en el Repository y corre en `Dispatchers.Default`.
- Las cargas tienen un límite de 2 segundos y reintentan hasta tres veces con espera creciente.
- El conteo de primos emite porcentajes y el resultado final desde un único `Flow` cancelable.
- El registro de prompts deja claro qué sugerencias aportó Codex; no se atribuyen respuestas a Gemini.
