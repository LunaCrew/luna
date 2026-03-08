package lunacrew.luna.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import lunacrew.luna.database.entities.AltCommunicationEntity

@Dao
interface AltCommunicationDao {
    @Insert
    suspend fun insertText(altCommunication: AltCommunicationEntity)

    @Update
    suspend fun updateText(altCommunication: AltCommunicationEntity)

    @Delete
    suspend fun deleteText(altCommunication: AltCommunicationEntity)

    @Query("SELECT * FROM alt_communication ORDER BY indexOrder ASC")
    fun getAllTexts(): Flow<List<AltCommunicationEntity>>
}
