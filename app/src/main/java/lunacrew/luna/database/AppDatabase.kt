package lunacrew.luna.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lunacrew.luna.database.dao.AltCommunicationDao
import lunacrew.luna.database.dao.UserDao
import lunacrew.luna.database.entities.UserEntity
import lunacrew.luna.database.entities.AltCommunicationEntity

@Database(
    entities = [
        UserEntity::class,
        AltCommunicationEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun altCommunicationDao(): AltCommunicationDao
}

object Database {
    @Volatile private var INSTANCE: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "luna-app-database"
            ).addMigrations()
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
