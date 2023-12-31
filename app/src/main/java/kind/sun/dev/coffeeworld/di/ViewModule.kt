package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kind.sun.dev.coffeeworld.util.custom.CustomLoadingDialog

@Module
@InstallIn(FragmentComponent::class)
object ViewModule {

    @Provides
    @FragmentScoped
    fun provideLoadingDialog(): CustomLoadingDialog {
        return CustomLoadingDialog()
    }

}