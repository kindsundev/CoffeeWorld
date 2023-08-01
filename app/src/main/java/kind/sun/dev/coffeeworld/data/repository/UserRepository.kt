package kind.sun.dev.coffeeworld.data.repository

import kind.sun.dev.coffeeworld.data.api.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {

}