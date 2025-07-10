package br.com.abreulucas.mobileproject2.features.diagnostic.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.abreulucas.mobileproject2.features.apiGemini.viewModel.GeminiViewModel
import br.com.abreulucas.mobileproject2.features.apiRoboflow.viewmodel.RoboflowViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import br.com.abreulucas.mobileproject2.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticoScreen(navController: NavController) {
    Scaffold(
    ) { innerPadding ->
        DiagnosticoContent(modifier = Modifier.padding(innerPadding), navController)
    }
}






