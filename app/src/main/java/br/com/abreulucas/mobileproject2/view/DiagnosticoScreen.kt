package br.com.abreulucas.mobileproject2.view

import android.content.Context
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.abreulucas.mobileproject2.features.apiGemini.viewModel.GeminiViewModel
import br.com.abreulucas.mobileproject2.features.apiRoboflow.viewmodel.RoboflowViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.Composable
import br.com.abreulucas.mobileproject2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticoScreen(
    roboflowViewModel: RoboflowViewModel = viewModel(),
    geminiViewModel: GeminiViewModel = viewModel(),
    context: Context
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val classificationResult by roboflowViewModel.result.collectAsState()
    val geminiResult by geminiViewModel.response.collectAsState()

    val isLoadingGemini by geminiViewModel.isLoading.collectAsState()
    val isLoadingRoboflow by roboflowViewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            roboflowViewModel.classifyImage(context = context, uri = it)
        }
    }

    LaunchedEffect(classificationResult) {
        classificationResult?.let {
            geminiViewModel.askGemini(
                "Após uma análise da imagem da pele, gerou o seguinte resultado: $it. Dê uma explicação, mais detalhes e dicas."
            )
        }
    }

    Column {
        LogoHeader()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Diagnóstico de Mancha na Pele")
                            Text(
                                "Este resultado não substitui uma avaliação médica.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SelectImageButton(onClick = { launcher.launch("image/*") })

                selectedImageUri?.let {
                    SelectedImagePreview(uri = it)
                }

                if (isLoadingRoboflow) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                } else {
                    classificationResult?.let {
                        ResultCard2(
                            title = "Resultado",
                            content = it,
                        )
                    }
                }

                if (isLoadingGemini) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                } else {
                    geminiResult?.let {
                        ResultCard2(title = "Análise Detalhada", content = it)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectImageButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text("Selecionar imagem")
    }
}

@Composable
fun SelectedImagePreview(uri: Uri) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ResultCard1(title: String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ResultCard2(title: String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LogoHeader() {
    Image(
        painter = painterResource(id = R.drawable.logo_derma_scan),
        contentDescription = "Logo DermaScan",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(top = 16.dp),
    )
}
