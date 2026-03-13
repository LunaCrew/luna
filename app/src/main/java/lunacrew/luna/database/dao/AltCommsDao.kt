package lunacrew.luna.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import lunacrew.luna.database.entities.AltCommsEntity

@Dao
interface AltCommsDao {
    @Insert
    suspend fun insertCard(altCommunication: AltCommsEntity)

    @Update
    suspend fun updateCard(altCommunication: AltCommsEntity)

    @Delete
    suspend fun deleteCard(altCommunication: AltCommsEntity)

    @Query("SELECT * FROM alt_communication ORDER BY position ASC")
    fun getAllCards(): Flow<List<AltCommsEntity>>
}
