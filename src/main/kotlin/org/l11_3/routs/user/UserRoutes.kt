package org.l11_3.routs.user

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.user(userController: UserController) {
    post("user/register") { userController.register(call) }

    post("user/login") { userController.login(call) }

    authenticate {
        post("user/edit") { userController.editData(call) }

        post("user/edit/password") { userController.updatePassword(call) }

        post("user/event/addAsOrganizer") {
            userController.addEventAsOrganizer(call)
        }

        post("user/event/addAsParticipant") {
            userController.addEventAsParticipant(call)
        }

        post("user/event/deleteAsOrganizer") {
            userController.deleteEventAsOrganizer(call)
        }

        post("user/event/deleteAsParticipant") {
            userController.deleteEventAsParticipant(call)
        }

        post("user/quit") { userController.quit(call) }
    }
}