package lunacrew.luna.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import lunacrew.luna.database.dao.AltCommsDao
import lunacrew.luna.database.dao.SettingsDao
import lunacrew.luna.database.entities.AltCommsEntity
import lunacrew.luna.database.entities.SettingsEntity
import javax.inject.Inject

@Database(
    entities = [
        AltCommsEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun altCommunicationDao(): AltCommsDao
    abstract fun settingsDao(): SettingsDao
}

class Database @Inject constructor() {
    @Volatile private var database: AppDatabase? = null
    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "luna-database"
            ).addMigrations(*getDatabaseMigrations())
                .fallbackToDestructiveMigration(true)
                .build()
            database = instance
            instance
        }
    }
}

fun getDatabaseMigrations(): Array<Migration> = arrayOf()
