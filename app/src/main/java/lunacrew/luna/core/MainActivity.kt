package lunacrew.luna.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import lunacrew.luna.core.composable.NavDrawer
import lunacrew.luna.core.composable.NavigationStack
import lunacrew.luna.core.composable.TopBar
import lunacrew.luna.database.backup.DatabaseBackupLauncher
import lunacrew.luna.database.backup.DatabaseBackupViewModel
import lunacrew.luna.ui.theme.LunaTheme
import lunacrew.luna.util.composables.colorScheme
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabaseClient: Provider<SupabaseClient>

    @Inject
    lateinit var databaseBackupViewModel: Provider<DatabaseBackupViewModel>

    @Inject
    lateinit var databaseBackupLauncher: Provider<DatabaseBackupLauncher>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.get().handleDeeplinks(intent)

        enableEdgeToEdge()

        setContent {
            databaseBackupViewModel.get().setImportDbLauncher(
                databaseBackupLauncher.get().registerImport(applicationContext)
            )
            databaseBackupViewModel.get().setExportDbLauncher(
                databaseBackupLauncher.get().registerExport(applicationContext)
            )

            val drawerState = rememberDrawerState(DrawerValue.Closed)

            LunaTheme {
                NavDrawer(drawerState) {
                    Scaffold(
                        contentWindowInsets = WindowInsets.safeContent,
                        topBar = { TopBar(drawerState) },
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier.padding(innerPadding),
                            color = colorScheme().background
                        ) {
                            NavigationStack()
                        }
                    }
                }
            }
        }
    }
}
