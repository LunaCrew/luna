package lunacrew.luna.alt_comms

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.util.accessibility.TtsViewModel
import lunacrew.luna.util.composables.WavyProgressIndicator

@Composable
fun AltCommsCard(
    card: AltCommsEntity,
    modifier: Modifier = Modifier,
    audioProgress: Float,
    isSpeaking: Boolean,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    ttsViewModel: TtsViewModel = hiltViewModel()
) {
    val clickedId = ttsViewModel.clickedId.collectAsState().value
    ElevatedCard(
        modifier = modifier.combinedClickable(
            onClick = { onClick() },
            onDoubleClick = { onDoubleClick() }
        )
    ) {
        ConstraintLayout(
            Modifier.fillMaxWidth()
        ) {
            val (emoji, text, progress) = createRefs()

            Text(
                text = card.emoji,
                modifier = Modifier.constrainAs(emoji) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = card.tts,
                modifier = Modifier.constrainAs(text) {
                    start.linkTo(parent.start)
                    top.linkTo(emoji.bottom, 16.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 16.dp)
                }
            )

            if (isSpeaking) {
                if (card.id == clickedId) {
                    WavyProgressIndicator(
                        audioProgress,
                        modifier = Modifier.constrainAs(progress) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        })
                }
            }
        }
    }
}
