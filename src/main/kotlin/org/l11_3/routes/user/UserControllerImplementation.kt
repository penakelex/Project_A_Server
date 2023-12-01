package org.l11_3.routes.user

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.l11_3.codes.cipher
import org.l11_3.codes.unCipher
import org.l11_3.database.models.Code
import org.l11_3.database.services.Service
import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.database.models.UserUpdate
import org.l11_3.database.states.EventState
import org.l11_3.responses.builders.toResultResponse
import org.l11_3.responses.builders.toResponse
import org.l11_3.responses.values.Result
import org.l11_3.routes.user.states.CodeType
import org.l11_3.session.JWTValues
import org.l11_3.session.generateToken
import org.l11_3.session.getPasswordString
import org.l11_3.session.getUserIDString

class UserControllerImplementation(private val service: Service, private val values: JWTValues) : UserController {

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

    override suspend fun addEventAsBobblehead(call: ApplicationCall) {
        call.respond(
            service.usersService.addEventAsBobblehead(
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

    override suspend fun deleteEventAsBobblehead(call: ApplicationCall) {
        call.respond(
            service.usersService.deleteEventAsBobblehead(
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt(),
                eventID = call.receive<UInt>()
            )
        )
    }

    override suspend fun generateCode(call: ApplicationCall) {
        val code = call.receive<Code>()
        val userID = call.principal<JWTPrincipal>()!!
            .payload.getClaim(getUserIDString()).asInt().toUInt()
        when (code.type) {
            CodeType.Event.value -> {
                call.respond(
                    (Result.OK to
                            "${CodeType.Event.value.cipher()}.${
                                userID.cipher()
                            }.${code.eventID!!.cipher()}.${code.eventState!!.cipher()}"
                            ).toResponse()
                )
            }

            CodeType.Profile.value -> {
                val (getInformationResult, userData) = service.usersService.getUserInformation(userID = userID)
                if (getInformationResult != Result.OK) {
                    call.respond((getInformationResult to null).toResponse())
                    return
                }
                call.respond(
                    (Result.OK to
                            "${CodeType.Profile.value.cipher()}.${
                                userID.cipher()
                            }.${userData!!.name.cipher()}.${
                                userData.surname.cipher()
                            }.${userData.patronymic.cipher()}.${
                                userData.status.cipher()
                            }").toResponse()
                )
            }
        }
        call.respond((Result.NoSuchTypeOfQRCode to null).toResponse())
    }

    override suspend fun validateCode(call: ApplicationCall) {
        val code = call.receive<String>().split(".").iterator()
        when (code.next().unCipher()) {
            CodeType.Event.value -> {
                call.respond(
                    service.usersService.checkUserIsRegisteredOnEvent(
                        userID = code.next().unCipher().toUInt(),
                        eventID = code.next().unCipher().toUInt(),
                        eventState = when (code.next().unCipher()) {
                            EventState.Organizer.value -> EventState.Organizer
                            EventState.Participant.value -> EventState.Bobblehead
                            else -> EventState.Bobblehead
                        }
                    ).toResultResponse()
                )
            }

            CodeType.Profile.value -> {
                call.respond(
                    service.usersService.checkUserData(
                        userID = code.next().unCipher().toUInt(),
                        name = code.next().unCipher(),
                        surname = code.next().unCipher(),
                        patronymic = code.next().unCipher(),
                        status = code.next().unCipher()
                    ).toResultResponse()
                )
            }
        }
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