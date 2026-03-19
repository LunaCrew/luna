package lunacrew.luna.util.extensions

/**
 * **If** [allowEmpty]:
 *
 * `true` -> checks maximum length.
 *
 * `false` -> checks if is not empty and maximum length.
 */
fun String.isValid(maxLength: Int, allowEmpty: Boolean = false): Boolean =
    if (allowEmpty) {
        this.length <= maxLength
    } else {
        this.isNotEmpty() && this.length <= maxLength
    }

