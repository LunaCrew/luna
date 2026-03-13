package lunacrew.luna.alt_comms

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import lunacrew.luna.R
import lunacrew.luna.util.composables.EmojiPicker
import lunacrew.luna.util.composables.colorScheme
import lunacrew.luna.util.composables.typography
import lunacrew.luna.util.models.AltCommCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltCommsDialog(
    initialText: String? = null,
    initialEmoji: String? = null,
    context: Context,
    onDismiss: () -> Unit,
    onConfirm: (AltCommCard) -> Unit
) {
    var content by remember { mutableStateOf(initialText ?: "") }
    val isValid = content.isNotBlank()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var error by remember { mutableStateOf(false) }
    var emoji by remember { mutableStateOf(initialEmoji ?: "\uD83D\uDC4B") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.7f else 0.9f)
                .wrapContentHeight()
                .imePadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                val (title, emojiField, emojiLabel, textField, buttons) = createRefs()

                Text(
                    text = stringResource(R.string.create_message),
                    style = typography().titleLarge,
                    color = colorScheme().primary,
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 16.dp)
                        end.linkTo(parent.end)
                    }
                )

                IconButton(
                    onClick = { showEmojiPicker = !showEmojiPicker },
                    colors = IconButtonColors(
                        contentColor = colorScheme().onSecondaryContainer,
                        containerColor = colorScheme().secondaryContainer,
                        disabledContentColor = colorScheme().onPrimaryContainer,
                        disabledContainerColor = colorScheme().primaryContainer,
                    ),
                    modifier = Modifier.constrainAs(emojiField) {
                        start.linkTo(parent.start)
                        top.linkTo(title.bottom, 16.dp)
                        end.linkTo(parent.end)
                    }
                ) {
                    Text(emoji)
                }

                Text(
                    text = stringResource(R.string.select_emoji),
                    color = colorScheme().primary,
                    modifier = Modifier.constrainAs(emojiLabel) {
                        start.linkTo(parent.start)
                        top.linkTo(emojiField.bottom)
                        end.linkTo(parent.end)
                    }
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { newValue ->
                        if (newValue.lines().size <= 6) {
                            content = newValue
                        } else {
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
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        top.linkTo(emojiLabel.bottom, 16.dp)
                        end.linkTo(parent.end)
                    },
                    singleLine = false,
                    maxLines = 6,
                )

                Row(
                    modifier = Modifier.constrainAs(buttons) {
                            top.linkTo(textField.bottom, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            bottom.linkTo(parent.bottom, 16.dp)
                        }
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            onConfirm(AltCommCard(emoji, content))
                        },
                        enabled = isValid,
                    ) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }

            if (showEmojiPicker) {
                ModalBottomSheet(
                    onDismissRequest = { showEmojiPicker = false },
                    sheetState = sheetState
                ) {
                    EmojiPicker(context) {
                        emoji = it
                        showEmojiPicker = false
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    AltCommsDialog("", "", LocalContext.current, {}) { }
}
