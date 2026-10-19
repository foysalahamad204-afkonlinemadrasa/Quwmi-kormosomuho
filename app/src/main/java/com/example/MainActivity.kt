package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.AuthViewModel
import com.example.ui.theme.KaomiTheme

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val noticeId = intent.getStringExtra("noticeId")

        setContent {
            val context = LocalContext.current
            val authViewModel: AuthViewModel = viewModel(
                factory = com.example.ui.auth.AuthViewModelFactory(context.applicationContext as android.app.Application)
            )
            val state by authViewModel.uiState.collectAsStateWithLifecycle()

            KaomiTheme(darkTheme = state.isDarkMode) {
                AuthScreen(
                    viewModel = authViewModel,
                    initialNoticeId = noticeId
                )
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val noticeId = intent.getStringExtra("noticeId")
        // Note: In a real app, you might want to trigger a state update here if the app is already running.
        // For now, restarting or recomposing with new intent is enough if setIntent is called.
    }
}
