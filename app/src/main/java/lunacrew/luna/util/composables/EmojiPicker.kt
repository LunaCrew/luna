package lunacrew.luna.util.composables

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.emoji2.emojipicker.EmojiPickerView

@SuppressLint("InflateParams")
@Composable
fun EmojiPicker(context: Context, onSelected: (String) -> Unit) {
    val scrollState = rememberScrollState()
    AndroidView(
        factory = { context ->
            EmojiPickerView(context).apply {
                setOnEmojiPickedListener { item ->
                    onSelected(item.emoji)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            ),
    ) {
        LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, null).apply {
            ViewCompat.setNestedScrollingEnabled(this, true)
        }

    }
}
