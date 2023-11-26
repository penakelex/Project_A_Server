package org.l11_3.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.l11_3.routs.user.UserController
import org.l11_3.routs.user.user

fun Application.configureRouting() {
    val userController by inject<UserController>()
    routing {
        user(userController)
    }
}
