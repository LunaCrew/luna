package lunacrew.luna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.room.RoomDatabase
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import lunacrew.luna.auth.Authentication
import lunacrew.luna.auth.SignInForm
import lunacrew.luna.database.Database
import lunacrew.luna.ui.theme.LunaTheme

class MainActivity : ComponentActivity() {
    lateinit var db: RoomDatabase
    private val authRegister = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
        Authentication.onSignInResult(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Database.getInstance(applicationContext)

        enableEdgeToEdge()
        setContent {
            LunaTheme {
                LunaApp()
            }
        }
    }

    @PreviewScreenSizes
    @Composable
    private fun LunaApp() {
        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Home) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        )

        SignInForm(Modifier.padding(top = 50.dp)) { Authentication(authRegister).launchSignIn() }
    }

    enum class AppDestinations(
        val label: String,
        val icon: ImageVector,
    ) {
        Menu("Menu", Icons.Default.Menu),
        Home("Home", Icons.Default.Home),
        Me("Me", Icons.Default.AccountCircle),
    }
}
