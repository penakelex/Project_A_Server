package org.l11_3.routes.user

import io.ktor.server.application.*

interface UserController {
    suspend fun register(call: ApplicationCall)
    suspend fun login(call: ApplicationCall)
    suspend fun editData(call: ApplicationCall)
    suspend fun updatePassword(call: ApplicationCall)
    suspend fun updateEmail(call: ApplicationCall)
    suspend fun addEventAsOrganizer(call: ApplicationCall)
    suspend fun addEventAsParticipant(call: ApplicationCall)
    suspend fun addEventAsBobblehead(call: ApplicationCall)
    suspend fun deleteEventAsOrganizer(call: ApplicationCall)
    suspend fun deleteEventAsParticipant(call: ApplicationCall)
    suspend fun deleteEventAsBobblehead(call: ApplicationCall)
    suspend fun generateCode(call: ApplicationCall)
    suspend fun validateCode(call: ApplicationCall)
    suspend fun quit(call: ApplicationCall)
}