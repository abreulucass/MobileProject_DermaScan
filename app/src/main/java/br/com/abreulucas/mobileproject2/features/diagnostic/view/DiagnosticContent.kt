package br.com.abreulucas.mobileproject2.features.diagnostic.view

import android.Manifest
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.abreulucas.mobileproject2.features.apiGemini.viewModel.GeminiViewModel
import br.com.abreulucas.mobileproject2.features.apiRoboflow.viewmodel.RoboflowViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.navigation.NavController
import br.com.abreulucas.mobileproject2.features.camera.view.CameraXScreen
import java.io.File

@Composable
fun DiagnosticoContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current

    val geminiViewModel: GeminiViewModel = viewModel()
    val roboflowViewModel: RoboflowViewModel = viewModel()

    val classificationResult by roboflowViewModel.result.collectAsState()
    val geminiResult by geminiViewModel.response.collectAsState()
    val isLoadingGemini by geminiViewModel.isLoading.collectAsState()
    val isLoadingRoboflow by roboflowViewModel.isLoading.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//    var photoUri: Uri? by remember { mutableStateOf(null) }

    var capturedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Estado para saber se a permissão já foi concedida
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PERMISSION_GRANTED
        )
    }

    // Launcher para pedir permissão da câmera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture()
//    ) { success ->
//        if (success) {
//            photoUri?.let {
//                selectedImageUri = it
//                roboflowViewModel.classifyImage(context, it)
//            }
//        }
//    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            roboflowViewModel.classifyImage(context, it)
        }
    }

    LaunchedEffect(classificationResult) {
        classificationResult?.let { geminiViewModel.askGemini(classificationResult!!) }
    }

    if (showCamera) {
        CameraXScreen(
            onImageCaptured = { uri ->
                capturedPhotoUri = uri
                selectedImageUri = uri
                roboflowViewModel.classifyImage(context, uri)
                showCamera = false
            },
            onError = { exception ->
                // Trate o erro aqui, talvez mostrar um Toast
                showCamera = false
            },
            onCancel = { showCamera = false },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Column(
            modifier = modifier
                .padding(24.dp)
                .fillMaxSize()
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Classificação de Mancha na Pele",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Selecione uma das opções a baixo para iniciar a classificação",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SelectImageButton(
                    onClick = { galleryLauncher.launch("image/*") },
                )

                CameraButton(
                    onClick = {
                        if (hasCameraPermission) {
                            showCamera = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                )

            }

            selectedImageUri?.let { SelectedImagePreview(it) }

            selectedImageUri?.let{
                if (isLoadingRoboflow) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                } else {
                    classificationResult?.let {
                        ResultCard(title = "Resultado", content = "${classificationResult!!.className} com uma confiança de ${classificationResult!!.confidence*100}%")

                        if (isLoadingGemini) {
                            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            geminiResult?.let {
                                ResultCard(title = "Análise Detalhada", content = it)
                            }
                        }
                    } ?: run{
                        ResultCard(title = "Resultado", content = "Nenhum Resultado Encontrado")
                    }
                }
            }

        }
    }
}

fun createImageUri(context: Context): Uri {
    val imageFile = File(context.cacheDir, "captured_image.jpg")
    if (imageFile.exists()) {
        imageFile.delete()
    }
    imageFile.createNewFile()

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}

