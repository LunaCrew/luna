package lunacrew.luna.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lunacrew.luna.alt_comms.AlternativeCommunicationScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            MainScreen(navController)
        }
        composable(route = Screen.AltComms.route) {
            AlternativeCommunicationScreen()
        }
    }
}
