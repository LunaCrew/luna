package lunacrew.luna.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * @return Composable string resource of given id.
 */
@Composable
fun Int.getString() = stringResource(this)
