SIGAAPP
=======

Descripción
-----------
SIGAAPP es una aplicación Android escrita en Kotlin y construida con Gradle. Contiene la lógica y recursos necesarios para compilar una APK de ejemplo y sirve como punto de partida para desarrollar funcionalidades de la aplicación móvil.

Características
---------------
- Basada en Kotlin y Jetpack (Android)
- Estructura estándar de Gradle para Android
- Ejemplos de pantallas y recursos en `app/src/main`

Requisitos
----------
- JDK 11 o superior
- Android SDK (herramientas de plataforma y build-tools correspondientes)
- Android Studio recomendado para desarrollo y depuración

Instalación y compilación
-------------------------
Desde la línea de comandos (Windows PowerShell o similar):

    cd C:\Users\hdagu\Documents\SIGAAPP
    ./gradlew clean assembleDebug

En sistemas Unix/macOS usar `./gradlew` (siempre con permisos de ejecución). Para abrir y ejecutar en un emulador o dispositivo físico, abra el proyecto en Android Studio y use las herramientas de ejecución (Run).

Ejecución
---------
- Para instalar la APK generada en un dispositivo conectado:

    ./gradlew installDebug

- Para ejecutar pruebas unitarias:

    ./gradlew testDebugUnitTest

- Para ejecutar pruebas instrumentadas (requiere emulador o dispositivo):

    ./gradlew connectedAndroidTest

