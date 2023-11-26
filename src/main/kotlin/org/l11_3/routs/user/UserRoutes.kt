package org.l11_3.routs.user

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.user(userController: UserController) {
    post("user/register") { userController.register(call) }

    post("user/login") { userController.login(call) }

    authenticate {
        post("user/edit") { userController.editData(call) }
    }
}