package com.example.sigaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sigaapp.data.local.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CardSize {
    SMALL, MEDIUM, LARGE
}

class SettingsViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _cardSize = MutableStateFlow(CardSize.MEDIUM)
    val cardSize: StateFlow<CardSize> = _cardSize.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val savedSize = sessionManager.getCardSize()
        _cardSize.value = try {
            CardSize.valueOf(savedSize)
        } catch (e: Exception) {
            CardSize.MEDIUM
        }
    }

    fun setCardSize(size: CardSize) {
        _cardSize.value = size
        sessionManager.saveCardSize(size.name)
    }

    fun getNotificationSettings() = sessionManager.getNotificationSettings()
    
    fun saveNotificationSettings(push: Boolean, stock: Boolean) {
        sessionManager.saveNotificationSettings(push, stock)
    }
}

class SettingsViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
