package lunacrew.luna.alt_comms

import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import lunacrew.luna.R
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.util.accessibility.textToSpeech
import lunacrew.luna.util.composables.colorScheme
import lunacrew.luna.util.composables.icons
import lunacrew.luna.util.composables.typography
import lunacrew.luna.util.extensions.getString
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeCommunicationScreen(
    viewModel: AltCommsViewModel = hiltViewModel()
) {
    var content by remember { mutableStateOf<AltCommsEntity?>(null) }
    var isAltCommDialogVisible by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isReorderEnabled by remember { mutableStateOf(false) }
    var isCloseBtnVisible by remember { mutableStateOf(false) }
    var isDeleteBtnVisible by remember { mutableStateOf(false) }
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val configuration = LocalConfiguration.current
        val itemSize = when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> DpSize(maxWidth * 0.3f, maxHeight * 0.3f)
            Configuration.ORIENTATION_LANDSCAPE -> DpSize(maxWidth * 0.3f, maxHeight * 0.5f)
            else -> DpSize(maxWidth * 0.3f, maxHeight * 0.4f)
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = itemSize.width),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            state = lazyGridState
        ) {
            items(cards, key = { it.id }) { item ->
                ReorderableItem(reorderableLazyGridState, key = item.id) {
                    val isSelected = selectedCardId == item.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemSize.height)
                            .zIndex(if (isSelected) 1f else 0f),
                        tonalElevation = 1.dp,
                        shadowElevation = 1.dp
                    ) {
                        // Box da borda (zIndex menor)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .zIndex(0f)
                                .combinedClickable(
                                    onClick = {
                                        tts.speak(item.text, TextToSpeech.QUEUE_FLUSH, null, null)
                                    },
                                    onLongClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedCardId = item.id

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
                            Text(
                                text = item.text,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(8.dp)
                            )
                        }
                        if (isDeleteBtnVisible) {
                            SmallFloatingActionButton(
                                onClick = {
                                    viewModel.deleteCard(item)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-6).dp, y = (-6).dp)
                                    .zIndex(1f)
                                    .size(40.dp),
                                shape = CircleShape,
                                containerColor = colorScheme().error,
                                contentColor = colorScheme().onError
                            ) {
                                Icon(
                                    imageVector = icons().Delete,
                                    contentDescription = R.string.remove.getString()
                                )
                            }
                        }
                        if (isSelected && !isReorderEnabled) {
                            SmallFloatingActionButton(
                                onClick = {
                                    content = item
                                    isEditMode = true
                                    isAltCommDialogVisible = true
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-4).dp, y = (-3).dp)
                                    .zIndex(1f)
                                    .size(40.dp),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = icons().Edit,
                                    contentDescription = R.string.edit.getString()
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!isCloseBtnVisible) {
            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                val (configButton, addButton) = createRefs()
                FloatingActionButton(
                    onClick = {
                        isMenuExpanded = !isMenuExpanded
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(configButton) {
                            end.linkTo(addButton.start)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Icon(imageVector = icons().Settings, contentDescription = R.string.alt_comms_settings.getString())
                }

                FloatingActionButton(
                    onClick = { isAltCommDialogVisible = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(addButton) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Icon(
                        imageVector = icons().Add,
                        contentDescription = R.string.add.getString()
                    )
                }
            }
        } else {
            FloatingActionButton(
                onClick = {
                    isCloseBtnVisible = false
                    isReorderEnabled = false
                    isDeleteBtnVisible = false
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = icons().Check,
                    contentDescription = R.string.confirm.getString()
                )
            }
        }


        DropdownMenu(
            expanded = isMenuExpanded,
            containerColor = colorScheme().primaryContainer,
            shape = BubbleShape(arrowHeight = 40f),
            onDismissRequest = { isMenuExpanded = false },
            offset = DpOffset((-75).dp, (-190).dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        style = typography().titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = colorScheme().onPrimaryContainer,
                        text = R.string.remove.getString()
                    )
                },
                onClick = {
                    isDeleteBtnVisible = true
                    isMenuExpanded = false
                    isCloseBtnVisible = true
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        style = typography().titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = colorScheme().onPrimaryContainer,
                        text = R.string.reorder.getString()
                    )
                },
                onClick = {
                    isReorderEnabled = true
                    isMenuExpanded = false
                    isCloseBtnVisible = true
                }
            )
        }

        if (isAltCommDialogVisible) {
            AltCommsDialog (
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
