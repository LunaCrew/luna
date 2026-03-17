package lunacrew.luna.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.backup.DatabaseBackup
import lunacrew.luna.util.devtools.SentryConfig
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _roomBkp = MutableLiveData<DatabaseBackup>()
    val roomBkp: LiveData<DatabaseBackup> = _roomBkp

    private val _db = MutableLiveData<AppDatabase>()
    val db: LiveData<AppDatabase> = _db

    private val _sentry = MutableLiveData<SentryConfig>()
    val sentry: LiveData<SentryConfig> = _sentry

    private val _navController = MutableLiveData<NavHostController>()
    val navController: LiveData<NavHostController> = _navController

    fun init(
        database: AppDatabase,
        databaseBackup: DatabaseBackup,
        sentryConfig: SentryConfig
    ) {
        _db.postValue(database)
        _roomBkp.postValue(databaseBackup)
        _sentry.postValue(sentryConfig)
    }

    fun saveNavController(navController: NavHostController) {
        _navController.postValue(navController)
    }
}
