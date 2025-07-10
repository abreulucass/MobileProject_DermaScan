package br.com.abreulucas.mobileproject2.features.history.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.abreulucas.mobileproject2.database.entity.Consulta
import br.com.abreulucas.mobileproject2.features.apiGemini.viewModel.GeminiViewModel
import br.com.abreulucas.mobileproject2.features.history.viewmodel.HistoryViewModel
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import br.com.abreulucas.mobileproject2.database.AppDatabase
import br.com.abreulucas.mobileproject2.features.history.viewmodel.HistoryViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val consultaDao = db.consultaDao()

    val factory = remember { HistoryViewModelFactory(consultaDao) }
    val historyViewModel: HistoryViewModel = viewModel(factory = factory)
    val consultas by historyViewModel.consultas.collectAsState()

    LaunchedEffect(Unit) {
        historyViewModel.carregarConsultas()
    }

    var onConsultaClick: (consulta: Consulta) -> Unit = {
        // código aqui
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Histórico de Consultas") }
            )
        }
    ) { paddingValues ->
        if (consultas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma consulta encontrada")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(consultas) { consulta ->
                    ConsultaItem(consulta = consulta, onClick = { onConsultaClick(consulta) })
                }
            }
        }
    }
}

@Composable
fun ConsultaItem(
    consulta: Consulta,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    // Como seu timeStamp é String, tenta converter para Date para formatar,
    // se não der, mostra o próprio timeStamp raw.
    val formattedDate = try {
        val parsedDate = dateFormat.parse(consulta.timeStamp)
        if (parsedDate != null) dateFormat.format(parsedDate) else consulta.timeStamp
    } catch (e: Exception) {
        consulta.timeStamp
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(consulta.imageUri),
                contentDescription = "Imagem da consulta",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = consulta.classname, style = MaterialTheme.typography.titleMedium)
                Text(text = "Confiança: ${(consulta.confidence.format(2))}%", style = MaterialTheme.typography.bodyMedium)
                Text(text = formattedDate, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Função de extensão para formatar Double com n casas decimais
fun Double.format(digits: Int) = "%.${digits}f".format(this)