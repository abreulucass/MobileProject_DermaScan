package br.com.abreulucas.mobileproject2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.abreulucas.mobileproject2.DisclaimerScreen
import br.com.abreulucas.mobileproject2.HistoricoScreen
import br.com.abreulucas.mobileproject2.HomeScreen
import br.com.abreulucas.mobileproject2.MainScreen
import br.com.abreulucas.mobileproject2.features.diagnostic.view.DiagnosticoScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "disclaimer", modifier = Modifier) {
        composable("disclaimer"){ DisclaimerScreen(navController) }
        composable("main") { MainScreen() }
    }
}

@Composable
fun BottonNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen() }
        composable("diagnostico") { DiagnosticoScreen(navController) }
        composable("historico") { HistoricoScreen() }
    }
}
