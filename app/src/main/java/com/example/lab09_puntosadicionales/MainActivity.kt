package com.example.lab09_puntosadicionales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.lab09_puntosadicionales.ui.AmphibiansScreen
import com.example.lab09_puntosadicionales.ui.AmphibiansViewModel
import com.example.lab09_puntosadicionales.ui.theme.AmphibiansTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as AmphibiansApplication).container.repository
        val viewModel = ViewModelProvider(
            this,
            AmphibiansViewModel.Factory(repository),
        )[AmphibiansViewModel::class.java]

        setContent {
            AmphibiansTheme {
                AmphibiansScreen(
                    uiState = viewModel.uiState,
                    onRetry = viewModel::refresh,
                )
            }
        }
    }
}
