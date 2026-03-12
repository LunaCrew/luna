package lunacrew.luna.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("settings")
data class SettingsEntity(
    @PrimaryKey
    val parameter: String,
    val value: String
)
