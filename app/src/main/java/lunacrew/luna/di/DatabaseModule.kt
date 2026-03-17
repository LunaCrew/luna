package lunacrew.luna.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lunacrew.luna.backup.RoomBackup
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.Database
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Database().getDatabase(context)

    @Provides
    @Singleton
    fun provideRoomBackup(db: Database, @ApplicationContext context: Context): RoomBackup =
        RoomBackup(db.getDatabase(context).openHelper)
}
