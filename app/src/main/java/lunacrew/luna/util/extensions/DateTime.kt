package lunacrew.luna.util.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTime {
    companion object {
        /**
         * Get current timestamp in format `yyyy-MM-dd-HHmmss`.
         *
         * **e.g.** `2026-03-16-230958`
         */
        fun getTimestamp(): String {
            val formatPattern = "yyyy-MM-dd-HHmmss"
            val formatter = DateTimeFormatter.ofPattern(formatPattern)
            val currentDateTime = LocalDateTime.now()
            val formattedTimestamp = currentDateTime.format(formatter)
            return formattedTimestamp
        }
    }
}
