package lunacrew.luna.database.dao

import androidx.room.Dao
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

    @Query("DELETE FROM alt_communication WHERE id IN (:cardIds)")
    suspend fun deleteCards(cardIds: List<Int?>)

    @Query("SELECT * FROM alt_communication ORDER BY position ASC")
    fun getAllCards(): Flow<List<AltCommsEntity>>
}
