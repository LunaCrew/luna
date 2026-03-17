package lunacrew.luna.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { navController.navigate(Screen.AltComms.route) },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Alt-comms")
        }
    }
}
