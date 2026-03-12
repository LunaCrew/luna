package lunacrew.luna.alternative.communication

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BubbleShape(private val arrowHeight: Float = 25f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val rectHeight = size.height - arrowHeight
            val width = size.width
            val centerX = width / 2f

            // 1. Desenha o corpo do balão (Retângulo Arredondado)
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, width, rectHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(40f, 40f)
                )
            )

            // 2. Desenha a seta (Triângulo) apontando para baixo
            moveTo(centerX - arrowHeight, rectHeight) // Início da base da seta
            lineTo(centerX, size.height)              // Ponta da seta
            lineTo(centerX + arrowHeight, rectHeight) // Fim da base da seta
            close()
        }
        return Outline.Generic(path)
    }
}
