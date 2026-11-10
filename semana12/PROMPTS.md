# 🤖 Registro de Prompts e Interacción con IA (Fase 2)

Este documento registra los prompts utilizados y el flujo de asistencia asistida por IA para las mejoras, refactorizaciones y pruebas unitarias de la aplicación **ComputerStore**.

---

## 📌 Prompt 1: Corrección de compilación e integración de Room Database
> **Prompt del usuario:**
> *"I'm getting the following error while building my project... Fix this issue. revisa el proyecto y avanza con H8 Room: Entity, Dao y Database. feat: base de datos Room"*

### 💡 Resultado y Solución con IA:
- Diagnóstico del error de plugin Gradle (configuración del compilador de anotaciones de Room en Gradle).
- Implementación y verificación de las clases `ItemEntity`, `ItemDao` y `AppDatabase`.
- Ajuste del repositorio `ItemRepositoryImpl` para usar Room como la única fuente de verdad (Single Source of Truth).

---

## 📌 Prompt 2: Modularización de Commits por Hito
> **Prompt del usuario:**
> *"pero revisa, hay archivos de h 9 hasta h 11... yo solo quiero los archivos para el h8 y hacerle su commit"*

### 💡 Resultado y Solución con IA:
- Identificación quirúrgica de los archivos correspondientes al Hito 8 (`ItemEntity.kt`, `ItemDao.kt`, `AppDatabase.kt`, `build.gradle.kts`, `app/build.gradle.kts`).
- Generación de comandos exactos para PowerShell sin errores de sintaxis (`\`).

---

## 📌 Prompt 3: Sincronización y Publicación en Rama Principal (`main`)
> **Prompt del usuario:**
> *"no quiero que subas a la rama mejora con IA estamos trabajando con la rama main del repo. Subes los comits 8 hasta el 11 al repo de github"*

### 💡 Resultado y Solución con IA:
- Transferencia limpia de commits H8 a H11 hacia la rama `main` mediante `cherry-pick`.
- Generación e integración de la documentación `README.md`.
- Publicación exitosa de todos los hitos H1 a H16 en `origin/main`.

---

## 📌 Prompt 4: Implementación de Pruebas Unitarias para ViewModels
> **Prompt del usuario:**
> *"la mejora con IA hazlo en la rama mejora con IA"*

### 💡 Resultado y Solución con IA:
- Creación de la rama `mejora-ia`.
- Configuración de dependencias de prueba (`kotlinx-coroutines-test`).
- Creación y ejecución exitosa de pruebas unitarias para `ListaViewModelTest` (`3 passed, 0 failed`).
