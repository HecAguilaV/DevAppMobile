package com.example.sigaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sigaapp.data.model.StockItem
import com.example.sigaapp.data.repository.SaaSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: SaaSRepository) : ViewModel() {
    private val _stockItems = MutableStateFlow<List<StockItem>>(emptyList())
    val stockItems: StateFlow<List<StockItem>> = _stockItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadInventory()
    }

    fun loadInventory() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getStock().fold(
                onSuccess = { items ->
                    _stockItems.value = items
                },
                onFailure = { e ->
                    _error.value = e.message ?: "Error al cargar inventario"
                    // Dummy data for fallback if requested, currently showing empty or error
                }
            )
            _isLoading.value = false
        }
    }
}
