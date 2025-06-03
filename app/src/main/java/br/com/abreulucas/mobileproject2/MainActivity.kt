// MainActivity.kt
package br.com.abreulucas.mobileproject2


import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import br.com.abreulucas.mobileproject2.features.apiRoboflow.viewmodel.RoboflowViewModel
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.abreulucas.mobileproject2.features.apiGemini.viewModel.GeminiViewModel
import br.com.abreulucas.mobileproject2.view.DiagnosticoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkinApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinApp() {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val geminiViewModel: GeminiViewModel = viewModel()
    val roboflowViewModel: RoboflowViewModel = viewModel()

    DiagnosticoScreen(
        roboflowViewModel = roboflowViewModel,
        geminiViewModel = geminiViewModel,
        context = context
    )
}

