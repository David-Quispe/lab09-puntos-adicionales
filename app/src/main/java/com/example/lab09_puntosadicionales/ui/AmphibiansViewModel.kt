package com.example.lab09_puntosadicionales.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab09_puntosadicionales.data.Amphibian
import com.example.lab09_puntosadicionales.data.AmphibiansRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

sealed interface AmphibiansUiState {
    data object Loading : AmphibiansUiState
    data class Success(val amphibians: List<Amphibian>) : AmphibiansUiState
    data object Error : AmphibiansUiState
}

class AmphibiansViewModel(
    private val repository: AmphibiansRepository,
) : ViewModel() {
    var uiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    init {
        refresh()
    }

    fun refresh() {
        uiState = AmphibiansUiState.Loading
        viewModelScope.launch {
            uiState = try {
                AmphibiansUiState.Success(repository.getAmphibians())
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                AmphibiansUiState.Error
            }
        }
    }

    class Factory(private val repository: AmphibiansRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(AmphibiansViewModel::class.java))
            return AmphibiansViewModel(repository) as T
        }
    }
}
