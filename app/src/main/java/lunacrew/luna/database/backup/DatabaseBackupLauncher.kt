package lunacrew.luna.database.backup

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.util.extensions.getActivity
import javax.inject.Inject

class DatabaseBackupLauncher @Inject constructor(
    private val viewModel: MainViewModel
) {
    @Composable
    fun registerExport(
        context: Context,
    ): ManagedActivityResultLauncher<String, Uri?> = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/zip"),
    ) { uri ->
        uri?.also { uri ->
            context.getActivity()?.let { activity ->
                viewModel.roomBkp.observe(activity) { roomBackup ->
                    roomBackup.export(context, uri)
                }
            }
        }
    }

    @Composable
    fun registerImport(
        context: Context,
    ): ManagedActivityResultLauncher<Array<String>, Uri?> = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.also { uri ->
            context.getActivity()?.let { activity ->
                viewModel.roomBkp.observe(activity) { roomBackup ->
                    roomBackup.import(context, uri, restart = true)
                }
            }
        }
    }
}
