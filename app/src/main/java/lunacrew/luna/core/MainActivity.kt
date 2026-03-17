package lunacrew.luna.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import lunacrew.luna.ui.theme.LunaTheme
import lunacrew.luna.util.composables.colorScheme
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabaseClient: Provider<SupabaseClient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.get().handleDeeplinks(intent)

        enableEdgeToEdge()

        setContent {
            LunaTheme {
                Scaffold(contentWindowInsets = WindowInsets.safeContent) { innerPadding ->
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
