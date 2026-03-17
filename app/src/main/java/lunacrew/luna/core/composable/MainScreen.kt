package lunacrew.luna.core.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import lunacrew.luna.database.backup.DatabaseBackupViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    databaseBackupViewModel: DatabaseBackupViewModel = hiltViewModel(),
) {
    var export by remember { mutableStateOf(false) }
    var import by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = { export = !export }
            ) {
                Text("Export db")
            }

            Button(
                onClick = { import = !import }
            ) {
                Text("Import db")
            }
        }
    }


    if (export) {
       databaseBackupViewModel.export(context)
    }

    if (import) {
        databaseBackupViewModel.import(context)
    }
}
