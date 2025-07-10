// MainActivity.kt
package br.com.abreulucas.mobileproject2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.abreulucas.mobileproject2.common.ui.theme.DermaScanTheme
import br.com.abreulucas.mobileproject2.navigation.AppNavigation
import br.com.abreulucas.mobileproject2.navigation.BottonNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DermaScanTheme{
                AppNavigation()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        BottonNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun DisclaimerScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aviso Importante",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Este aplicativo tem finalidade educacional e informativa. Ele não substitui uma consulta, diagnóstico ou tratamento médico profissional. Consulte um dermatologista para avaliação adequada.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                navController.navigate("main") {
                    popUpTo("disclaimer") { inclusive = true }
                }
            }) {
                Text("Entendi")
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LogoHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Bem-vindo ao DermaScan",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Este aplicativo ajuda a analisar imagens de lesões de pele utilizando inteligência artificial. " +
                    "Lembre-se: ele não substitui a avaliação de um dermatologista.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Conheça os principais tipos de lesões analisadas:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        DiseaseInfoCard(
            title = "Melanoma",
            description = "Tipo mais agressivo de câncer de pele. Pode surgir como uma pinta nova ou mudar uma já existente.",
            imageRes = R.drawable.melanoma
        )

        DiseaseInfoCard(
            title = "Carcinoma Basocelular",
            description = "Tipo comum e de crescimento lento. Aparece como uma lesão perolada ou ferida que não cicatriza.",
            imageRes = R.drawable.cacinoma
        )

        DiseaseInfoCard(
            title = "Lesões Benignas",
            description = "Incluem pintas (nevos), ceratoses e dermatofibromas. Não são cancerígenas, mas devem ser monitoradas.",
            imageRes = R.drawable.benigno
        )
    }
}

@Composable
fun DiseaseInfoCard(title: String, description: String, imageRes: Int? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)

            imageRes?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "$title exemplo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}



data class BottomNavItem(val label: String, val route: String, val icon: Int)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", "home", R.drawable.home_24px),
        BottomNavItem("Detecção", "diagnostico", R.drawable.search_24px),
        BottomNavItem("Histórico", "historico", R.drawable.history_24px)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.height(56.dp) // altura mais compacta
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp),
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp, // fonte menor
                        color = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
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

@Composable
fun HistoricoScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Tela de Histórico")
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SkinApp() {
//    val context = LocalContext.current
//    val application = context.applicationContext as Application
//
//    val geminiViewModel: GeminiViewModel = viewModel()
//    val roboflowViewModel: RoboflowViewModel = viewModel()
//
//    DiagnosticoScreen(
//        roboflowViewModel = roboflowViewModel,
//        geminiViewModel = geminiViewModel,
//        context = context
//    )
//}

