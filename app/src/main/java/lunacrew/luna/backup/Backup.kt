package lunacrew.luna.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.util.extensions.DateTime

@Composable
fun exportDatabase(
    viewModel: MainViewModel = hiltViewModel()
): String {
    val context = LocalContext.current

    val exportDbLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("application/zip"),
        ) {
            it?.also { uri ->
                viewModel.roomBkp?.export(context, uri)
            }
        }

    val timestamp = DateTime.getTimestamp()
    val filename = "luna-exported-$timestamp"
    exportDbLauncher.launch(filename)
    return filename
}

@Composable
fun ImportDatabase(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val importDbLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument(),
        ) {
            it?.also { uri ->
                viewModel.roomBkp?.import(context, uri, restart = true)
            }
        }

    importDbLauncher.launch(arrayOf("application/zip"))
}
