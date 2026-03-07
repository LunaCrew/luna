package lunacrew.luna.alternative.communication

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import lunacrew.luna.R

@Composable
fun AddEditDialog(
    initialText: String? = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){
    var content by remember { mutableStateOf(initialText) }
    val isValid = content?.isNotBlank()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var error by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.7f else 0.9f)
                .wrapContentHeight()
                .imePadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.add_text),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                content?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { newValue ->
                            if (newValue.lines().size <= 6) {
                                content = newValue
                            }else{
                                error = true
                            }
                        },
                        isError = error,
                        supportingText = {
                            if (error) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.line_limit),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.enter_text)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 6,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            content?.let { onConfirm(it) }
                        },
                        enabled = isValid == true,
                    ) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }
        }
    }
}
