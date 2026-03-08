package lunacrew.luna.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("alt_communication")
data class AltCommunicationEntity (
    @PrimaryKey(autoGenerate = true) val textId: Int,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "indexOrder") val order: Int
)
