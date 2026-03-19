package lunacrew.luna.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.util.accessibility.TtsViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel = MainViewModel()

    @Provides
    @Singleton
    fun provideTtsViewModel(): TtsViewModel = TtsViewModel()
}
