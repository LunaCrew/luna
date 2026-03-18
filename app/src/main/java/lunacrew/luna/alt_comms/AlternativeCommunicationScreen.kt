package lunacrew.luna.alt_comms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.R
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.util.accessibility.TtsViewModel
import lunacrew.luna.util.composables.FloatingButton
import lunacrew.luna.util.extensions.getDrawable
import lunacrew.luna.util.extensions.getString
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeCommunicationScreen(
    altCommsViewModel: AltCommsViewModel = hiltViewModel(),
    ttsViewModel: TtsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var isAltCommDialogVisible by remember { mutableStateOf(false) }
    var lastId: Int? by remember { mutableStateOf(null) }
    val cardsState by altCommsViewModel.cardsList.collectAsState()
    val cards = remember(cardsState) {
        mutableStateListOf<AltCommsEntity>().apply { addAll(cardsState) }
    }
    val haptics = LocalHapticFeedback.current
    var content by remember { mutableStateOf<AltCommsEntity?>(null) }
    var selectedCardId by rememberSaveable { mutableStateOf<Int?>(null) }

    lastId = cards.toList().lastOrNull()?.id
    val tts = ttsViewModel.tts.collectAsState().value

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
            altCommsViewModel.reorderCards(cards)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { selectedCardId = null })
            .scrollable(
                rememberScrollState(),
                orientation = Orientation.Vertical
            )
    ) {
        val lazyGridState = rememberLazyGridState()
        val state = rememberReorderableLazyGridState(lazyGridState) { from, to ->
            cards.add(to.index, cards.removeAt(from.index))
            haptics.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(148.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            state = lazyGridState,
            modifier = Modifier
                .fillMaxSize()
                .heightIn(148.dp)
                .padding(8.dp)
        ) {
            items(cards, key = { it.id ?: 0 }) { item ->
                val isSelected = selectedCardId == item.id
                ReorderableItem(state, key = item.id ?: 0) {
                    AltCommsCard(
                        card = item,
                        onClick = {
                            ttsViewModel.talk(context, item.tts)
                            ttsViewModel.setClickedId(item.id)
                        },
                        onDoubleClick = { selectedCardId = item.id },
                        audioProgress = ttsViewModel.progress.collectAsState().value,
                        isSpeaking = tts?.isSpeaking ?: false,
                        modifier = Modifier
                            .draggableHandle(
                                onDragStarted = {
                                    haptics.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                },
                                onDragStopped = {
                                    altCommsViewModel.reorderCards(cards.toList())
                                    haptics.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                },
                            )

                    )

                    if (isSelected) {
                        SmallFloatingActionButton(
                            onClick = {
                                altCommsViewModel.deleteCards(item.id)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .zIndex(1f)
                                .size(40.dp),
                            shape = CircleShape
                        ) {
                            Icon(
                                painter = R.drawable.delete_filled.getDrawable(),
                                contentDescription = R.string.delete_selected.getString()
                            )
                        }

                        SmallFloatingActionButton(
                            onClick = {
                                content = item
                                isAltCommDialogVisible = true
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .zIndex(1f)
                                .size(40.dp),
                            shape = CircleShape
                        ) {
                            Icon(
                                painter = R.drawable.edit_filled.getDrawable(),
                                contentDescription = R.string.edit.getString()
                            )
                        }
                    }
                }
            }
        }

        FloatingButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            icon = R.drawable.add_filled.getDrawable(),
            description = R.string.add.getString()
        ) {
            isAltCommDialogVisible = true
        }
    }

    if (isAltCommDialogVisible) {
        AltCommsDialog(
            content = content,
            context = context,
            lastId = lastId,
            onDismiss = {
                isAltCommDialogVisible = false
                selectedCardId = null
            },
        )
    }
}
