package lunacrew.luna.core.composable

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lunacrew.luna.account.AccountScree
import lunacrew.luna.alt_comms.AlternativeCommunicationScreen
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.core.Screen
import lunacrew.luna.medical.MedicalScreen
import lunacrew.luna.notepad.NotepadScreen
import lunacrew.luna.pomodoro.PomodoroScreen
import lunacrew.luna.settings.SettingsScreen

@Composable
fun NavigationStack(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) { MainScreen(navController) }
        composable(Screen.AltComms.route) { AlternativeCommunicationScreen() }
        composable(Screen.Account.route) { AccountScree(navController) }
        composable(Screen.Medical.route) { MedicalScreen(navController) }
        composable(Screen.Notepad.route) { NotepadScreen(navController) }
        composable(Screen.Pomodoro.route) { PomodoroScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }

    viewModel.saveNavController(navController)
}
