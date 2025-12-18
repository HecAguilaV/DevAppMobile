#!/bin/bash
echo "🚀 Iniciando Generación de APK Firmada..."
echo "📂 Keystore: ../siga-release.jks (Configurado en keystore.properties)"

export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr"
export ANDROID_HOME="/c/Users/hdagu/AppData/Local/Android/Sdk"
export PATH="$JAVA_HOME/bin:$PATH"

# Ejecutar tarea de release
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo " "
    echo "✅ ¡APK Firmada Generada con Éxito!"
    echo "📍 Ubicación: SIGA APP/build/outputs/apk/release/"
    echo " "
    # Listar el archivo generado para confirmar
    ls -lh "SIGA APP/build/outputs/apk/release/"
else
    echo "❌ Error al generar la APK."
fi
