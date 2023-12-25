package kind.sun.dev.coffeeworld.util.custom

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithAuth

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WithoutAuth