package lunacrew.luna.util.models

/**
 * @property emoji Unicode value of emoji. See [EmojiPedia](https://emojipedia.org/).
 * @property text Value to be synthesized.
 * @property id Local id.
 * @property position Item index.
 */
data class AltCommCard(
    val emoji: String,
    val text: String,
    val id: Int? = null,
    val position: Int? = null
)
