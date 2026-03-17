package lunacrew.luna.util.composables

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun FloatingButton(
    modifier: Modifier,
    icon: Painter,
    description: String,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = icon,
            contentDescription = description
        )
    }
}
