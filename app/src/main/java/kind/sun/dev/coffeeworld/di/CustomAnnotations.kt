package kind.sun.dev.coffeeworld.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithAuthQualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithoutAuthQualifier