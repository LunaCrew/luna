package lunacrew.luna.core

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import lunacrew.luna.R
import lunacrew.luna.alt_comms.AlternativeCommunicationScreen
import lunacrew.luna.util.extensions.getString

class Navigator {
    private var current: NavDestination? = null
    private lateinit var navHost: NavHostController

    @Composable
    fun Init() {
        navHost = rememberNavController()
        val navBackStackEntry by navHost.currentBackStackEntryAsState()
        current = navBackStackEntry?.destination
    }

    /**
     * Keeps the backstack intact.
     */
    @Composable
    fun Navigate(route: Routes) {
        navHost.navigate(route)
    }

    /**
     * Clears backstack.
     */
    @Composable
    fun Navigate(route: Routes, popSelf: Boolean, saveCurrentState: Boolean) {
        navHost.navigate(route) {
            popUpTo(route) {
                inclusive = popSelf
                saveState = saveCurrentState
            }
        }
    }

    @Composable
    fun BottomNav() {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                BottomNavRoutes.entries.forEach { element ->
                    item(
                        icon = {
                            Icon(
                                element.icon,
                                contentDescription = element.labelId.getString()
                            )
                        },
                        label = { Text(element.labelId.getString()) },
                        modifier = Modifier.padding(top = 8.dp),
                        selected = current?.hierarchy?.any { it.route == it.label } == true,
                        onClick = {
                            navHost.navigate(element.labelId) {
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
                startDestination = BottomNavRoutes.Home.name
            ) {
                composable(route = BottomNavRoutes.Menu.name) { }
                composable(route = BottomNavRoutes.Home.name) {
                    AlternativeCommunicationScreen()
                }
                composable(route = BottomNavRoutes.Profile.name) { }
            }
        }
    }

    private enum class BottomNavRoutes(
        val labelId: Int,
        val icon: ImageVector,
    ) {
        Menu(R.string.menu, Icons.Default.Menu),
        Home(R.string.home, Icons.Default.Home),
        Profile(R.string.profile, Icons.Default.AccountCircle),
    }
}
