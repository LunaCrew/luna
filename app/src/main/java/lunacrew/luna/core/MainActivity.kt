package lunacrew.luna.core

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import lunacrew.luna.alternative.communication.AlternativeCommunicationScreen
import lunacrew.luna.ui.theme.LunaTheme
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var supabaseClient: Provider<SupabaseClient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.get().handleDeeplinks(intent)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            LunaTheme {
                LunaApp()
            }

//            ShowSystemUi()
        }
    }

    @PreviewScreenSizes
    @Composable
    private fun LunaApp() {
        val navHost = rememberNavController()
        val navBackStackEntry by navHost.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach { element ->
                    item(
                        icon = {
                            Icon(
                                element.icon,
                                contentDescription = element.label
                            )
                        },
                        label = { Text(element.label) },
                        modifier = Modifier.padding(top = 8.dp),
                        selected = currentDestination?.hierarchy?.any { it.route == it.label } == true,
                        onClick = {
                            navHost.navigate(element.label) {
                                popUpTo(navHost.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) {
            NavHost(
                navController = navHost,
                startDestination = AppDestinations.Home.label
            ) {
                composable(route = AppDestinations.Menu.label) { }
                composable(route = AppDestinations.Home.label) {
                    AlternativeCommunicationScreen() }
                composable(route = AppDestinations.Me.label) { }
            }
        }

    }

    enum class AppDestinations(
        val label: String,
        val icon: ImageVector,
    ) {
        Menu("Menu", Icons.Default.Menu),
        Home("Home", Icons.Default.Home),
        Me("Me", Icons.Default.AccountCircle),
    }

    @Composable
    private fun ShowSystemUi() {
        val view = LocalView.current
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.show(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}
