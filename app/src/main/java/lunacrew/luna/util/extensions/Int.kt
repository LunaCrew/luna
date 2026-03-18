package lunacrew.luna.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * @return Composable string resource of given id.
 */
@Composable
fun Int.getString() = stringResource(this)

@Composable
fun Int.getDrawable() = painterResource(this)

fun Int?.next(): Int = if (this == null) 0 else this + 1

/**
 * Calculates a progress value between 0.0 and 1.0.
 * @param total The maximum value representing 1.0.
 * @return A Float between 0.0 and 1.0.
 */
fun Int.normalizeToFloat(total: Int): Float {
    if (total == 0) return 0f
    return this.toFloat() / total.toFloat()
}
