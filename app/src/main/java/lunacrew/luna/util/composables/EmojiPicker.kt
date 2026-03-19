package lunacrew.luna.util.composables

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView

@SuppressLint("InflateParams")
@Composable
fun EmojiPicker(onSelected: (String) -> Unit) {
    AndroidView(
        factory = { context ->
            val container = object : FrameLayout(context) {
                override fun onInterceptTouchEvent(ev: MotionEvent?) = false
                override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                    ev?.let { parent?.requestDisallowInterceptTouchEvent(true) }
                    return super.dispatchTouchEvent(ev)
                }
            }

            val emojiPicker = EmojiPickerView(context).apply {
                setOnEmojiPickedListener { item ->
                    onSelected(item.emoji)
                }
            }

            container.addView(
                emojiPicker,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            container
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
