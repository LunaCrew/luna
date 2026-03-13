package lunacrew.luna.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("alt_communication")
data class AltCommsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "emoji") val emoji: String
)
