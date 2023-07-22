package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModule {

    @Provides
    @Singleton
    fun provideLoadingDialog(): LoadingDialog {
        return LoadingDialog()
    }

}