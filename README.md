SIGAAPP
=======

Resumen
-------
Proyecto Android (Kotlin, Gradle) — importado al repositorio remoto https://github.com/HecAguilaV/DevAppMobile.git.

Flujo de trabajo (simple, solo `main`)
------------------------------------
1. Trabaja localmente en tu rama `main` (pruebas/iteraciones).
2. Cuando estés listo para subir todo al remoto, ejecuta:

   git add -A
   git commit -m "<mensaje descriptivo>"
   git push hecdev main

Esto empujará tus cambios directamente a `main` en el remoto `hecdev`.

Estado actual
-------------
- Remote: hecdev -> https://github.com/HecAguilaV/DevAppMobile.git
- Rama principal local: `main` (configurada para trackear `hecdev/main`)
- Se creó también una rama de importación remota con el contenido original: `import/sigaapp-<timestamp>` (histórico)

Cómo compilar y ejecutar (rápido)
--------------------------------
Requisitos: JDK 11+, Android SDK y Android Studio o las herramientas de línea de comandos de Gradle.

Desde PowerShell (Windows):

    cd C:\Users\hdagu\Documents\SIGAAPP
    ./gradlew assembleDebug

Para ejecutar en un dispositivo/emulador desde Android Studio, abre el proyecto y usa Run.

Siguientes pasos (opcional)
---------------------------
- Añadir GitHub Actions para CI (build en cada push).
- Crear un `CHANGELOG.md` o etiquetas semánticas para releases.

Contacto
--------
Repositorio propiedad de: HecAguilaV
Correo local configurado para commits: he.aguila@duocuc.cl

