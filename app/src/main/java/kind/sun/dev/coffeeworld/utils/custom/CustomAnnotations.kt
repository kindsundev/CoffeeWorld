package kind.sun.dev.coffeeworld.utils.custom

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithAuthQualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithoutAuthQualifier