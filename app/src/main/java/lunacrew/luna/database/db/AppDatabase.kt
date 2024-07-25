package lunacrew.luna.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import lunacrew.luna.database.dao.UserDao
import lunacrew.luna.database.entity.User
import kotlin.concurrent.Volatile

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?

    companion object {
        @Volatile
        private var database: AppDatabase? = null
        private const val DB_NAME = "luna.db"

        fun initDatabase(context: Context): AppDatabase? {
            if (database == null) {
                synchronized(AppDatabase::class.java) {
                    if (database == null) {
                        database =
                            databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                DB_NAME,
                            ).build()
                    }
                }
            }
            return database
        }
    }
}
