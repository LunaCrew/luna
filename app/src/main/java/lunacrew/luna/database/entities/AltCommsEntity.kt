package lunacrew.luna.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("alt_communication")
data class AltCommsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "tts") val tts: String,
    @ColumnInfo(name = "emoji") val emoji: String
)
