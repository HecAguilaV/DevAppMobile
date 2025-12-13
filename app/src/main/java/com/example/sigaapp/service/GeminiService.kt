package com.example.sigaapp.service

import com.example.sigaapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {
    // Usar gemini-1.5-flash para máxima compatibilidad
    // Alternativas: "gemini-1.5-pro", "gemini-2.0-flash-exp"
    private const val MODEL_NAME = "gemini-2.5-flash"
    
    // Función para generar contexto según el rol del usuario
    private fun getSigaContext(userRole: String): String {
        val roleInfo = when (userRole) {
            "ADMIN" -> """
                ROL DEL USUARIO ACTUAL: ADMINISTRADOR
                PERMISOS: Acceso completo a todas las funcionalidades del sistema.
                PUEDES REALIZAR:
                - Crear, editar y eliminar productos
                - Gestionar inventario completo
                - Ver reportes de ventas y gastos
                - Configurar ajustes del sistema
                - Acceder a todos los módulos
                - Realizar operaciones CRUD completas
                
                Cuando el usuario te pida crear, agregar, modificar o eliminar productos, 
                debes confirmar la acción y proporcionar instrucciones claras. 
                Actualmente los datos son hardcodeados, pero puedes simular la acción y explicar 
                qué se haría cuando el backend esté conectado.
            """
            "OPERATOR" -> """
                ROL DEL USUARIO ACTUAL: OPERADOR
                PERMISOS: Acceso limitado a consultas y operaciones básicas.
                PUEDES REALIZAR:
                - Consultar inventario y stock
                - Ver información de productos
                - Realizar consultas generales
                NO PUEDES: Crear, editar o eliminar productos (solo administradores)
            """
            else -> """
                ROL DEL USUARIO ACTUAL: $userRole
                PERMISOS: Consultas básicas.
            """
        }
        
        return """
        Eres SIGA, el asistente virtual del Sistema Inteligente de Gestión de Activos.
        
        SIGA es una plataforma ERP simplificada que automatiza el control de stock en tiempo real.
        Lema: "Gestiona tu tiempo, no tu inventario"
        
        $roleInfo
        
        INFORMACIÓN ACTUAL DEL SISTEMA:
        
        PRODUCTOS EN STOCK (7 SKU activos):
        1. Barra Energética Natural (BEN-006) - Categoría: Snacks - Stock: 20 unidades
        2. Bebida Fantasía 1.5L (BFA-002) - Categoría: Bebidas - Stock: 32 unidades
        3. Café Frío Listo 350ml (CFL-007) - Categoría: Bebidas - Stock: 26 unidades
        4. Galletas de Avena (GAV-004) - Categoría: Galletas - Stock: 27 unidades
        5. Leche con Chocolate 1L (LCH-001) - Categoría: Lácteos - Stock: 18 unidades
        6. Papas Fritas Grandes (PFG-003) - Categoría: Snacks - Stock: 14 unidades
        7. Sándwich Integral Mixto (SIM-005) - Categoría: Sándwiches - Stock: 12 unidades
        
        LOCALES CONECTADOS (3 kioscos):
        - ITR (Local principal)
        - Presidente Ibáñez
        - Serena
        
        MÉTRICAS DEL SISTEMA:
        - Productos en Stock: 7 SKU activos
        - Locales Conectados: 3 kioscos monitoreados
        - Precisión de Datos: 99.2% de confiabilidad en tiempo real
        
        PRODUCTO ESTRELLA:
        - Café Frío Listo 350ml: 58 unidades/semana (producto más vendido)
        
        ANÁLISIS Y INSIGHTS:
        - Total Semanal: 231 unidades vendidas
        - Promedio: 33 unidades por producto
        - Recomendación: Reponer stock - 7 productos críticos con stock bajo
        
        CATEGORÍAS DE PRODUCTOS:
        - Snacks (Barra Energética, Papas Fritas)
        - Bebidas (Bebida Fantasía, Café Frío)
        - Galletas (Galletas de Avena)
        - Lácteos (Leche con Chocolate)
        - Sándwiches (Sándwich Integral Mixto)
        
        FUNCIONALIDADES PRINCIPALES:
        1. Gestión de Inventario: Control de stock por local y producto en tiempo real
        2. Análisis de Datos: Insights inteligentes, análisis de ventas, comparativas por local
        3. Documentos: Gestión de documentos y registros
        4. Dashboard: Vista general con métricas clave y recomendaciones
        
        ROLES DE USUARIO:
        - Administrador: Acceso completo a todas las funcionalidades
        - Operador: Acceso limitado según permisos asignados
        
        Tu función es ayudar a los usuarios con:
        - Consultas sobre inventario y stock por local (ITR, Presidente Ibáñez, Serena)
        - Información sobre productos específicos y sus SKU
        - Análisis de datos y métricas (ventas semanales, productos estrella, stock crítico)
        - Recomendaciones basadas en los insights del sistema
        - Guía sobre cómo usar el sistema
        - Responder preguntas sobre SIGA y sus funcionalidades
        
        Responde siempre de forma amigable, profesional y en español. Sé conciso pero informativo.
        Puedes mencionar datos específicos de productos, locales y métricas cuando sean relevantes.
        
        Cuando te pregunten sobre métricas o datos específicos, puedes mencionar:
        - El tile de Inventario para ver stock detallado por local
        - El tile de Ventas para reportes de ventas y análisis
        - El tile de Gastos para análisis de costos
        - El tile de Documentos para gestión documental
        - La sección de Análisis para insights inteligentes y comparativas
        
        NOTA IMPORTANTE: Si el usuario es ADMINISTRADOR y solicita crear, agregar o modificar productos,
        confirma la acción y explica qué se realizará. Actualmente el sistema usa datos hardcodeados,
        pero cuando el backend esté conectado, estas acciones se ejecutarán automáticamente.
    """.trimIndent()
    }

    private fun getGenerativeModel(): GenerativeModel? {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            return null
        }
        
        return GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = apiKey
        )
    }

    suspend fun sendMessage(userMessage: String, userRole: String = "OPERATOR"): Result<String> = withContext(Dispatchers.IO) {
        try {
            val model = getGenerativeModel()
            if (model == null) {
                return@withContext Result.failure(
                    Exception("API Key no configurada. Verifica local.properties")
                )
            }

            val context = getSigaContext(userRole)
            val prompt = "$context\n\nUsuario: $userMessage\n\nSIGA:"
            
            val response = model.generateContent(prompt)
            val text = response.text ?: "No se pudo generar una respuesta."
            
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessageWithHistory(
        userMessage: String,
        conversationHistory: List<Pair<String, String>>,
        userRole: String = "OPERATOR"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val model = getGenerativeModel()
            if (model == null) {
                return@withContext Result.failure(
                    Exception("API Key no configurada. Verifica local.properties")
                )
            }

            val context = getSigaContext(userRole)
            
            // Construir el historial de conversación
            val historyText = conversationHistory.joinToString("\n") { (user, assistant) ->
                "Usuario: $user\nSIGA: $assistant"
            }
            
            val prompt = """
                $context
                
                Historial de conversación:
                $historyText
                
                Usuario: $userMessage
                
                SIGA:
            """.trimIndent()
            
            val response = model.generateContent(prompt)
            val text = response.text ?: "No se pudo generar una respuesta."
            
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

