package lunacrew.luna.util.extensions

import androidx.navigation.NavHostController

fun NavHostController.popAllTo(route: String) = this.navigate(route) {
    popUpTo(0) { inclusive = true }
}
