package lunacrew.luna.alt_comms

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.R
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.util.accessibility.textToSpeech
import lunacrew.luna.util.composables.FloatingButton
import lunacrew.luna.util.composables.colorScheme
import lunacrew.luna.util.extensions.getDrawable
import lunacrew.luna.util.extensions.getString
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeCommunicationScreen(
    viewModel: AltCommsViewModel = hiltViewModel(),
) {
    var content by remember { mutableStateOf<AltCommsEntity?>(null) }
    var isAltCommDialogVisible by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isReorderEnabled by remember { mutableStateOf(false) }
    var isDeletion by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    val checkedItems = remember { mutableStateListOf<Int?>() }
    var selectedCardId by rememberSaveable { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val cardsState by viewModel.cardsList.collectAsState()
    val cards = remember(cardsState) {
        mutableStateListOf<AltCommsEntity>().apply { addAll(cardsState) }
    }
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        cards.add(to.index, cards.removeAt(from.index))
        haptics.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    val tts = remember { textToSpeech(context) }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { selectedCardId = null })
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            state = lazyGridState
        ) {
            items(cards, key = { it.id }) { item ->
                ReorderableItem(reorderableLazyGridState, key = item.id) {
                    val isSelected = selectedCardId == item.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                    ) {
                        /**
                         * Handle card selection
                         */
                        selectedCardId = selectCard(
                            tts,
                            item,
                            haptics,
                            selectedCardId,
                            isReorderEnabled,
                            viewModel,
                            cards,
                            isChecked,
                            checkedItems
                        )

                        if (isSelected && !isReorderEnabled) {
                            SmallFloatingActionButton(
                                onClick = {
                                    content = item
                                    isEditMode = true
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
        }

        /**
         * Default buttons
         */
        AnimatedVisibility(
            visible = !isDeletion,
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Column {
                AnimatedVisibility(visible = isMenuExpanded) {
                    Column {
                        AssistChip(
                            onClick = {
                                isMenuExpanded = false
                                isDeletion = true
                            },
                            label = { Text(R.string.delete.getString()) },
                            leadingIcon = { Icon(R.drawable.delete_filled.getDrawable(), R.string.delete.getString()) },
                            modifier = Modifier.padding(8.dp),
                        )

                        AssistChip(
                            onClick = {
                                isMenuExpanded = false
                                isReorderEnabled = true
                            },
                            label = { Text(R.string.reorder.getString()) },
                            leadingIcon = {
                                Icon(
                                    painter = R.drawable.reorder.getDrawable(),
                                    contentDescription = R.string.reorder.getString(),
                                    tint = colorScheme().onSurface
                                )
                            },
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }

                FloatingButton(
                    modifier = Modifier.padding(8.dp).align(Alignment.End),
                    icon = R.drawable.more_vertical.getDrawable(),
                    description = R.string.menu.getString()
                ) {
                    isMenuExpanded = !isMenuExpanded
                }

                FloatingButton(
                    modifier = Modifier.padding(8.dp).align(Alignment.End),
                    icon = R.drawable.add_filled.getDrawable(),
                    description = R.string.add.getString()
                ) {
                    isMenuExpanded = false
                    isAltCommDialogVisible = true
                }
            }

            /**
             * Deletion mode buttons
             */
            AnimatedVisibility(
                visible = isDeletion,
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Column {
                    FloatingButton(
                        modifier = Modifier.padding(8.dp),
                        icon = R.drawable.delete_filled.getDrawable(),
                        description = R.string.delete_selected.getString()
                    ) {
                        checkedItems.toMutableList().removeIf { it == null }
                        if (checkedItems.isNotEmpty()) {
                            viewModel.deleteCards(checkedItems.toList())
                        }
                    }

                    FloatingButton(
                        modifier = Modifier.padding(8.dp),
                        icon = R.drawable.close.getDrawable(),
                        description = R.string.cancel.getString()
                    ) {
                        isMenuExpanded = false
                        isDeletion = false
                        checkedItems.clear()
                    }
                }
            }

            /**
             * Dialog component to add or edit card.
             */
            if (isAltCommDialogVisible) {
                AltCommsDialog(
                    initialText = content?.text,
                    initialEmoji = content?.emoji,
                    context = context,
                    onDismiss = {
                        content = null
                        isEditMode = false
                        isAltCommDialogVisible = false
                    },
                    onConfirm = { card ->
                        if (isEditMode && selectedCardId != null) {
                            val currentItem = cards.find { it.id == selectedCardId }
                            viewModel.updateCard(
                                AltCommsEntity(
                                    id = selectedCardId!!,
                                    text = card.text,
                                    emoji = card.emoji,
                                    position = currentItem?.position ?: 0
                                )
                            )
                            selectedCardId = null
                            isEditMode = false
                            content = null
                        } else {
                            viewModel.insertCard(
                                AltCommsEntity(
                                    id = 0,
                                    text = content?.text ?: "",
                                    position = cards.size,
                                    emoji = content?.emoji ?: ""
                                )
                            )
                        }
                        isAltCommDialogVisible = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ReorderableCollectionItemScope.selectCard(
    tts: TextToSpeech,
    item: AltCommsEntity,
    haptics: HapticFeedback,
    selectedCardId: Int?,
    isReorderEnabled: Boolean,
    viewModel: AltCommsViewModel,
    cards: SnapshotStateList<AltCommsEntity>,
    isChecked: Boolean,
    checkedItems: SnapshotStateList<Int?>
): Int? {
    var selectedCardId1 = selectedCardId
    var isChecked1 = isChecked
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = {
                    tts.speak(item.text, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    selectedCardId1 = item.id
                }
            )
            .draggableHandle(
                enabled = isReorderEnabled,
                onDragStarted = {
                    haptics.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                },
                onDragStopped = {
                    viewModel.reorderCards(cards)
                    haptics.performHapticFeedback(HapticFeedbackType.GestureEnd)
                },
            )
    ) {
        val (emoji, text, checkbox) = createRefs()
        Text(
            text = item.emoji,
            modifier = Modifier.constrainAs(emoji) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, 8.dp)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = item.text,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start)
                top.linkTo(emoji.bottom, 8.dp)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )

        Checkbox(
            checked = isChecked1,
            onCheckedChange = { checked ->
                isChecked1 = !isChecked1
                if (checked) {
                    checkedItems.add(item.id)
                } else {
                    checkedItems.remove(item.id)
                }
            },
            modifier = Modifier.constrainAs(checkbox) {
                top.linkTo(text.bottom, 8.dp)
                end.linkTo(parent.end, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            }
        )
    }
    return selectedCardId1
}
