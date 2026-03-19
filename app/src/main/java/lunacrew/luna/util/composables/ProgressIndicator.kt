package lunacrew.luna.util.composables

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WavyProgressIndicator(progress: Float = 1F, modifier: Modifier) {
    LinearWavyProgressIndicator(
        progress = { progress },
        modifier = modifier,
        color = WavyProgressIndicatorDefaults.indicatorColor,
        trackColor = WavyProgressIndicatorDefaults.trackColor,
        wavelength = 20.dp,
        waveSpeed = 40.dp,
    )
}

