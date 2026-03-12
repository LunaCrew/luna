package lunacrew.luna.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lunacrew.luna.util.constants.TableSettings
import lunacrew.luna.util.devtools.SentryConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    @Singleton
    fun provideSentry(@ApplicationContext context: Context): SentryConfig =
        SentryConfig(context)

    @Provides
    @Singleton
    fun provideTableSettings(): TableSettings = TableSettings
}
