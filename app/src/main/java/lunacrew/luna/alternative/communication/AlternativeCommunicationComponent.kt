package lunacrew.luna.alternative.communication

import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import lunacrew.luna.R
import lunacrew.luna.database.entities.AltCommunicationEntity
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeCommunicationScreen(
    alternativeCommunicationViewModel: AlternativeCommunicationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }


    var selectedTextId by rememberSaveable { mutableStateOf<Int?>(null) }
    val haptics = LocalHapticFeedback.current
    var oldContent: String? by remember { mutableStateOf("") }
    var editComponent by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var reorder by remember { mutableStateOf(false) }
    var closeFunction by remember { mutableStateOf(false) }
    var removeIcon by remember { mutableStateOf(false) }

    val boxTextsState by alternativeCommunicationViewModel.boxTextState.collectAsState()
    val boxTexts = remember(boxTextsState) {
        mutableStateListOf<AltCommunicationEntity>().apply { addAll(boxTextsState) }
    }


    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
//        boxTexts = boxTexts.apply {
//            this[to.index] = this[from.index].also {
//                this[from.index] = this[to.index]
//            }
//        }
        boxTexts.add(to.index, boxTexts.removeAt(from.index))

        haptics.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    val tts = remember {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.language = Locale.getDefault()
            }
        }
        ttsInstance
    }

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
            items(boxTexts, key = { it.textId }) { item ->
                ReorderableItem(reorderableLazyGridState, key = item.textId) {
                    val isSelected = selectedTextId == item.textId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemSize.height)
                            .zIndex(if (isSelected) 1f else 0f)
                    ) {
                        // Box da borda (zIndex menor)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .zIndex(0f)
                                .border(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
                                )
                                .combinedClickable(
                                    onClick = {
                                        tts.speak(item.text, TextToSpeech.QUEUE_FLUSH, null, null)
                                    },
                                    onLongClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedTextId = item.textId

                                    }
                                )
                                .draggableHandle(
                                    enabled = reorder,
                                    onDragStarted = {
                                        haptics.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                    },
                                    onDragStopped = {
                                        alternativeCommunicationViewModel.updateAllTextsOrder(boxTexts)
                                        haptics.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                    },
                                )
                        ) {
                            item.text?.let { text ->
                                Text(
                                    text = text,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(8.dp)
                                )
                            }
                        }
                        if (removeIcon) {
                            SmallFloatingActionButton(
                                onClick = {
                                    alternativeCommunicationViewModel.deleteText(item)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-6).dp, y = (-6).dp)
                                    .zIndex(1f)
                                    .size(40.dp),
                                shape = CircleShape,
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Item"
                                )
                            }
                        }
                        if (isSelected && !reorder) {
                            SmallFloatingActionButton(
                                onClick = {
                                    oldContent = item.text
                                    editComponent = true
                                    showDialog = true
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-4).dp, y = (-3).dp)
                                    .zIndex(1f)
                                    .size(40.dp),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Text"
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!closeFunction) {
            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                val (configButton, addButton) = createRefs()
                FloatingActionButton(
                    onClick = {
                        menuExpanded = !menuExpanded
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(configButton) {
                            end.linkTo(addButton.start)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Setting Menu")
                }

                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(addButton) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Text")
                }
            }
        } else {
            FloatingActionButton(
                onClick = {
                    closeFunction = false
                    reorder = false
                    removeIcon = false
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }


        DropdownMenu(
            expanded = menuExpanded,
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Muda a cor de fundo do menu
            shape = BubbleShape(arrowHeight = 40f),
            onDismissRequest = { menuExpanded = false },
            offset = DpOffset((-75).dp, (-190).dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = stringResource(R.string.remove)
                    )
                },
                onClick = {
                    removeIcon = true
                    menuExpanded = false
                    closeFunction = true
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = stringResource(R.string.reorder)
                    )
                },
                onClick = {
                    reorder = true
                    menuExpanded = false
                    closeFunction = true
                }
            )
        }

        if (showDialog) {
            AddEditDialog(
                initialText = oldContent ?: "",
                onDismiss = {
                    oldContent = ""
                    editComponent = false
                    showDialog = false
                },
                onConfirm = { content ->
                    if (editComponent && selectedTextId != null) {
                        val currentItem = boxTexts.find { it.textId == selectedTextId }
                        alternativeCommunicationViewModel.updateText(
                            AltCommunicationEntity(
                                textId = selectedTextId!!,
                                text = content,
                                order = currentItem?.order ?: 0
                            )
                        )
                        selectedTextId = null
                        editComponent = false
                        oldContent = ""
                    } else {
                        alternativeCommunicationViewModel.insertText(
                            AltCommunicationEntity(
                                textId = 0,
                                text = content,
                                order = boxTexts.size
                            )
                        )
                    }
                    showDialog = false
                }
            )
        }
    }
}
