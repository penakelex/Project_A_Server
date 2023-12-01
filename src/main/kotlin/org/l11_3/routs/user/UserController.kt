package org.l11_3.routs.user

import io.ktor.server.application.*

interface UserController {
    suspend fun register(call: ApplicationCall)
    suspend fun login(call: ApplicationCall)
    suspend fun editData(call: ApplicationCall)
    suspend fun updatePassword(call: ApplicationCall)
    suspend fun updateEmail(call: ApplicationCall)
    suspend fun addEventAsOrganizer(call: ApplicationCall)
    suspend fun addEventAsParticipant(call: ApplicationCall)
    suspend fun deleteEventAsOrganizer(call: ApplicationCall)
    suspend fun deleteEventAsParticipant(call: ApplicationCall)
    suspend fun quit(call: ApplicationCall)
}