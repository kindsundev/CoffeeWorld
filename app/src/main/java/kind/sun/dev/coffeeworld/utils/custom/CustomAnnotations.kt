package kind.sun.dev.coffeeworld.utils.custom

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithAuth

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithoutAuth