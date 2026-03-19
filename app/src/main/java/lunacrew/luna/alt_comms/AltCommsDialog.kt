package lunacrew.luna.alt_comms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.R
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.util.composables.EmojiPicker
import lunacrew.luna.util.composables.colorScheme
import lunacrew.luna.util.composables.typography
import lunacrew.luna.util.composables.validate
import lunacrew.luna.util.extensions.getString
import lunacrew.luna.util.extensions.isValid
import lunacrew.luna.util.extensions.next

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltCommsDialog(
    lastId: Int?,
    onDismiss: () -> Unit,
    content: AltCommsEntity? = null,
    viewModel: AltCommsViewModel = hiltViewModel()
) {
    val ttsMaxLength = 240
    var ttsLength by remember { mutableIntStateOf(content?.tts?.length ?: 0) }

    var tts by remember { mutableStateOf(content?.tts ?: "") }
    var emoji by remember { mutableStateOf(content?.emoji ?: "\uD83D\uDC4B") }
    val id = content?.id ?: lastId?.next()
    val isValid = tts.isValid(ttsMaxLength)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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
        Surface(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.7f else 0.9f)
                .imePadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                val (titleLabel, emojiField, emojiLabel, ttsField, buttons) = createRefs()

                Text(
                    text = R.string.create_message.getString(),
                    style = typography().titleLarge,
                    color = colorScheme().primary,
                    modifier = Modifier.constrainAs(titleLabel) {
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
                        top.linkTo(titleLabel.bottom, 16.dp)
                        end.linkTo(parent.end)
                    }
                ) {
                    Text(emoji)
                }

                Text(
                    text = R.string.select_emoji.getString(),
                    color = colorScheme().primary,
                    modifier = Modifier.constrainAs(emojiLabel) {
                        start.linkTo(parent.start)
                        top.linkTo(emojiField.bottom)
                        end.linkTo(parent.end)
                    }
                )

                OutlinedTextField(
                    value = tts,
                    onValueChange = { newValue ->
                        tts = newValue
                        ttsLength = newValue.length
                    },
                    supportingText = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "$ttsLength/$ttsMaxLength",
                            color = validate(tts.isValid(ttsMaxLength, true))
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.enter_text_to_be_said)) },
                    modifier = Modifier.constrainAs(ttsField) {
                        start.linkTo(parent.start)
                        top.linkTo(emojiLabel.bottom, 16.dp)
                        end.linkTo(parent.end)
                    },
                    singleLine = false,
                )

                Row(
                    modifier = Modifier.constrainAs(buttons) {
                        top.linkTo(ttsField.bottom, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                        bottom.linkTo(parent.bottom, 16.dp)
                    }
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                    ) {
                        Text(R.string.cancel.getString())
                    }
                    TextButton(
                        onClick = {
                            viewModel.insertCard(AltCommsEntity(id, tts, emoji))
                            onDismiss()
                        },
                        enabled = isValid,
                    ) {
                        Text(R.string.add.getString())
                    }
                }
            }

            if (showEmojiPicker) {
                ModalBottomSheet(
                    onDismissRequest = { showEmojiPicker = false },
                    sheetState = sheetState
                ) {
                    EmojiPicker {
                        emoji = it
                        showEmojiPicker = false
                    }
                }
            }
        }
    }
}
