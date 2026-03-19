package lunacrew.luna.database.backup

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lunacrew.luna.util.extensions.DateTime
import lunacrew.luna.util.extensions.getActivity
import javax.inject.Inject

@HiltViewModel
class DatabaseBackupViewModel @Inject constructor() : ViewModel() {
    private val _exportDbLauncher = MutableLiveData<ManagedActivityResultLauncher<String, Uri?>>()
    val exportDbLauncher: LiveData<ManagedActivityResultLauncher<String, Uri?>> = _exportDbLauncher

    private val _importDbLauncher = MutableLiveData<ManagedActivityResultLauncher<Array<String>, Uri?>>()
    val importDbLauncher: LiveData<ManagedActivityResultLauncher<Array<String>, Uri?>> = _importDbLauncher

    fun setExportDbLauncher(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        _exportDbLauncher.postValue(launcher)
    }

    fun setImportDbLauncher(launcher: ManagedActivityResultLauncher<Array<String>, Uri?>) {
        _importDbLauncher.postValue(launcher)
    }

    fun export(context: Context) {
        val timestamp = DateTime.getTimestamp()
        val filename = "luna-exported-$timestamp"
        context.getActivity()?.let { activity ->
            exportDbLauncher.observe(activity) { launcher ->
                launcher.launch(filename)
            }
        }
    }

    fun import(context: Context) {
        context.getActivity()?.let { activity ->
            importDbLauncher.observe(activity) { launcher ->
                launcher.launch(arrayOf("application/zip"))
            }
        }
    }
}
