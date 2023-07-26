package kind.sun.dev.coffeeworld.data.repository

import kind.sun.dev.coffeeworld.data.api.CafeService
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeService: CafeService
) {

}