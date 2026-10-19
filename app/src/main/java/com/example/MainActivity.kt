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

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val noticeId = intent.getStringExtra("noticeId")

        setContent {
            val context = LocalContext.current
            var authViewModel by remember { mutableStateOf<AuthViewModel?>(null) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                try {
                    authViewModel = com.example.ui.auth.AuthViewModelFactory(context.applicationContext as android.app.Application)
                        .create(AuthViewModel::class.java)
                } catch (e: Exception) {
                    error = "অ্যাপ্লিকেশন লোড করতে ব্যর্থ হয়েছে: ${e.localizedMessage ?: "Unknown Error"}"
                }
            }

            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(text = error!!)
                }
            } else if (authViewModel != null) {
                val state by authViewModel!!.uiState.collectAsStateWithLifecycle()
                KaomiTheme(darkTheme = state.isDarkMode) {
                    AuthScreen(
                        viewModel = authViewModel!!,
                        initialNoticeId = noticeId
                    )
                }
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
