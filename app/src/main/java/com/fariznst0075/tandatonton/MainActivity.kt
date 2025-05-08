package com.fariznst0075.tandatonton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fariznst0075.tandatonton.ui.screen.MainScreen
import com.fariznst0075.tandatonton.ui.theme.TandaTontonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TandaTontonTheme {
                MainScreen()
            }
        }
    }
}