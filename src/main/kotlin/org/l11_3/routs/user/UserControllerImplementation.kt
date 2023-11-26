package org.l11_3.routs.user

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.l11_3.database.services.Service
import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.database.models.UserUpdate
import org.l11_3.session.getUserIDString

class UserControllerImplementation(private val service: Service) : UserController {
    override suspend fun register(call: ApplicationCall) {
        call.respond(service.userService.register(user = call.receive<UserRegister>()))
    }

    override suspend fun login(call: ApplicationCall) {
        call.respond(service.userService.login(user = call.receive<UserLogin>()))
    }

    override suspend fun editData(call: ApplicationCall) {
        call.respond(
            service.userService.editData(
                user = call.receive<UserUpdate>(),
                id = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt()
            )
        )
    }
}