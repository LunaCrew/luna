package lunacrew.luna.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import lunacrew.luna.database.entities.SettingsEntity

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings ")
    fun getAll(): List<SettingsEntity>

    @Query("SELECT * FROM settings WHERE parameter LIKE :paramName LIMIT 1")
    fun findByName(paramName: String): SettingsEntity

    @Insert
    fun insertAll(vararg settings: SettingsEntity)

    @Delete
    fun delete(settings: SettingsEntity)
}
