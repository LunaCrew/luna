package lunacrew.luna.util.composables

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun colorScheme(): ColorScheme = MaterialTheme.colorScheme

@Composable
fun validate(condition: Boolean): Color =
    if (condition) colorScheme().onSurface else colorScheme().error

@Composable
fun typography(): Typography = MaterialTheme.typography

@Composable
fun shapes(): Shapes = MaterialTheme.shapes
