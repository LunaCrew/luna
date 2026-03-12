package lunacrew.luna.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import lunacrew.luna.database.dao.AltCommunicationDao
import lunacrew.luna.database.dao.SettingsDao
import lunacrew.luna.database.entities.AltCommunicationEntity
import lunacrew.luna.database.entities.SettingsEntity

@Database(
    entities = [
        AltCommunicationEntity::class,
        SettingsEntity::class
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun altCommunicationDao(): AltCommunicationDao
    abstract fun settingsDao(): SettingsDao
}

object Database {
    @Volatile private var INSTANCE: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "luna-app-database"
            ).addMigrations(*getDatabaseMigrations())
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance
        }
    }
}

fun getDatabaseMigrations(): Array<Migration> = arrayOf()
