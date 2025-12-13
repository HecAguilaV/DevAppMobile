package com.example.sigaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sigaapp.data.model.StockItem
import com.example.sigaapp.data.repository.SaaSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

import com.example.sigaapp.data.model.Local

class InventoryViewModel(private val repository: SaaSRepository) : ViewModel() {
    private val _stockItems = MutableStateFlow<List<StockItem>>(emptyList())
    // Expose filtered list or handle filtering in UI? Better here.
    // But we need the raw list to filter locally.
    private val _rawStockItems = MutableStateFlow<List<StockItem>>(emptyList())
    
    private val _locales = MutableStateFlow<List<Local>>(emptyList())
    val locales: StateFlow<List<Local>> = _locales
    
    private val _selectedLocal = MutableStateFlow<Local?>(null)
    val selectedLocal: StateFlow<Local?> = _selectedLocal
    
    // The exposed stockItems will be the filtered one
    val stockItems: StateFlow<List<StockItem>> = _selectedLocal
        .combine(_rawStockItems) { local, items ->
            if (local == null) items else items.filter { it.local_id == local.id }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating

    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Load Locales
            repository.getLocales().onSuccess { 
                _locales.value = it 
            }
            // Load Stock
            repository.getStock().fold(
                onSuccess = { items ->
                    _rawStockItems.value = items
                },
                onFailure = { e ->
                    _error.value = e.message ?: "Error al cargar inventario"
                }
            )
            _isLoading.value = false
        }
    }

    fun loadInventory() {
        // Alias for refresh
        loadData()
    }
    
    fun selectLocal(local: Local?) {
        _selectedLocal.value = local
    }

    fun addProduct(nombre: String, precio: Int, descripcion: String?) {
        viewModelScope.launch {
            _isCreating.value = true
            repository.createProduct(nombre, precio, descripcion).fold(
                onSuccess = {
                    loadInventory() // Recargar para ver el nuevo producto (aunque sea stock 0)
                },
                onFailure = { e ->
                    // Por ahora mostramos error genérico, idealmente un evento de UI único
                    _error.value = "Error al crear producto: ${e.message}"
                }
            )
            _isCreating.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
