package lunacrew.luna.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lunacrew.luna.backup.RoomBackup
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.util.devtools.SentryConfig
import lunacrew.luna.util.extensions.processLifecycleScope
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var database: Provider<AppDatabase>

    @Inject
    lateinit var sentryConfig: Provider<SentryConfig>

    @Inject
    lateinit var roomBackup: Provider<RoomBackup>

    @Inject
    lateinit var mainViewModel: Provider<MainViewModel>



    override fun onCreate() {
        super.onCreate()
        processLifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.get().init(
                database.get(),
                roomBackup.get(),
                sentryConfig.get(),
            )
        }
    }
}
