package lunacrew.luna.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lunacrew.luna.backup.RoomBackup
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.util.devtools.SentryConfig
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _roomBkp = MutableLiveData<RoomBackup?>()
    val roomBkp: RoomBackup? = _roomBkp.value

    private val _db = MutableLiveData<AppDatabase>()
    val db: AppDatabase? = _db.value

    private val _sentry = MutableLiveData<SentryConfig>()
    val sentry: SentryConfig? = _sentry.value

    fun init(
        database: AppDatabase,
        roomBackup: RoomBackup,
        sentryConfig: SentryConfig
    ) {
        _db.postValue(database)
        _roomBkp.postValue(roomBackup)
        _sentry.postValue(sentryConfig)
    }
}
