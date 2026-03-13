package lunacrew.luna.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import lunacrew.luna.ui.theme.LunaTheme
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var supabaseClient: Provider<SupabaseClient>
    @Inject lateinit var navigator: Provider<Navigator>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.get().handleDeeplinks(intent)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            navigator.get().Init()
            LunaTheme {
                navigator.get().BottomNav()
            }
        }
    }
}
