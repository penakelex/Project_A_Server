package org.l11_3.routs.user

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.l11_3.database.Service
import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister

class UserControllerImplementation(private val service: Service) : UserController {
    override suspend fun sendVerificationMessage(call: ApplicationCall) {
        
    }
    override suspend fun register(call: ApplicationCall) {
        call.respond(service.userService.register(user = call.receive<UserRegister>()))
    }

    override suspend fun login(call: ApplicationCall) {
        call.respond(service.userService.login(user = call.receive<UserLogin>()))
    }
}