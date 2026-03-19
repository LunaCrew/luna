package lunacrew.luna.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.Database
import lunacrew.luna.database.backup.DatabaseBackup
import lunacrew.luna.database.backup.DatabaseBackupLauncher
import lunacrew.luna.database.backup.DatabaseBackupViewModel
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
    fun provideRoomBackup(db: Database, @ApplicationContext context: Context): DatabaseBackup =
        DatabaseBackup(db.getDatabase(context).openHelper)

    @Provides
    @Singleton
    fun provideDatabaseBackupViewModel() = DatabaseBackupViewModel()

    @Provides
    @Singleton
    fun provideDatabaseBackupLauncher(viewModel: MainViewModel) = DatabaseBackupLauncher(viewModel)
}
