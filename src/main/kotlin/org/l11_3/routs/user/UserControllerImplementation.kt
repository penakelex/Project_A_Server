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
import org.l11_3.responses.builders.toResultResponse
import org.l11_3.responses.builders.toResponse
import org.l11_3.responses.values.Result
import org.l11_3.session.JWTValues
import org.l11_3.session.generateToken
import org.l11_3.session.getPasswordString
import org.l11_3.session.getUserIDString

class UserControllerImplementation(private val service: Service, private val values: JWTValues) : UserController {
    override suspend fun sendVerificationCode(call: ApplicationCall) {
        //TODO: почта
    }

    override suspend fun register(call: ApplicationCall) {
        val (registerResult, userSecurity) = service.usersService
            .register(user = call.receive<UserRegister>())
        if (registerResult != Result.OK) call.respond((registerResult to null).toResponse())
            .also { return }
        call.respond(
            (registerResult to generateToken(
                values = values, id = userSecurity!!.id,
                userPassword = userSecurity.password
            )).toResponse()
        )
    }

    override suspend fun login(call: ApplicationCall) {
        val (loginResult, userSecurity) = service.usersService
            .login(user = call.receive<UserLogin>())
        if (loginResult != Result.OK) call.respond((loginResult to null).toResponse())
            .also { return }
        call.respond(
            (loginResult to generateToken(
                values = values, id = userSecurity!!.id,
                userPassword = userSecurity.password
            )).toResponse()
        )
    }

    override suspend fun editData(call: ApplicationCall) {
        call.respond(
            service.usersService.editData(
                user = call.receive<UserUpdate>(),
                id = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt()
            ).toResultResponse()
        )
    }

    override suspend fun updatePassword(call: ApplicationCall) {
        val principalPayload = call.principal<JWTPrincipal>()!!.payload
        val id = principalPayload.getClaim(getUserIDString())
            .asInt().toUInt()
        val newPassword = call.receive<String>()
        val updateResult = service.usersService.updatePassword(
            newPassword = newPassword, id = id,
            password = principalPayload.getClaim(getPasswordString())
                .asInt()
        )
        if (updateResult != Result.OK) call.respond((updateResult to null).toResponse())
            .also { return }
        call.respond(
            (Result.OK to generateToken(
                values = values, id = id, userPassword = newPassword.hashCode()
            )).toResponse()
        )
    }

    override suspend fun updateEmail(call: ApplicationCall) {
        call.respond(
            service.usersService.updateEmail(
                newEmail = call.receive<String>(),
                id = call.principal<JWTPrincipal>()!!.payload
                    .getClaim(getUserIDString()).asInt().toUInt()
            ).toResultResponse()
        )
    }

    override suspend fun addEventAsOrganizer(call: ApplicationCall) {
        call.respond(
            service.usersService.addEventAsOrganizer(
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt(),
                eventID = call.receive<UInt>()
            ).toResultResponse()
        )
    }

    override suspend fun addEventAsParticipant(call: ApplicationCall) {
        call.respond(
            service.usersService.addEventAsParticipant(
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt(),
                eventID = call.receive<UInt>()
            ).toResultResponse()
        )
    }

    override suspend fun deleteEventAsOrganizer(call: ApplicationCall) {
        call.respond(
            service.usersService.deleteEventAsOrganizer(
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt(),
                eventID = call.receive<UInt>()
            ).toResultResponse()
        )
    }

    override suspend fun deleteEventAsParticipant(call: ApplicationCall) {
        call.respond(
            service.usersService.deleteEventAsParticipant(
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt(),
                eventID = call.receive<UInt>()
            ).toResultResponse()
        )
    }

    override suspend fun quit(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()!!
        call.respond(
            service.usersService.quit(
                userID = principal.payload.getClaim(getUserIDString())
                    .asInt().toUInt(),
                password = principal.payload.getClaim(getPasswordString())
                    .asInt()
            ).toResultResponse()
        )
    }
}