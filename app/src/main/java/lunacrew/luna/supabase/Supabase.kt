package lunacrew.luna.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import lunacrew.luna.BuildConfig

val supabaseClient = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
) {
    install(Auth) {
        host = "lunacrew.luna"
        scheme = "auth"
        defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
    }
    install(Storage)
    install(Realtime)
}
