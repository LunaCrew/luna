package lunacrew.luna.database.db

import android.app.Application

class InitDb : Application() {
    private lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.initDatabase(this)!!
    }
}
