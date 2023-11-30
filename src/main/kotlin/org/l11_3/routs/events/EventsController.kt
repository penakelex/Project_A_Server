package org.l11_3.routs.events

import io.ktor.server.application.*

interface EventsController {
    suspend fun create(call: ApplicationCall)
    suspend fun editData(call: ApplicationCall)
    suspend fun delete(call: ApplicationCall)
    suspend fun addParticipant(call: ApplicationCall)
    suspend fun removeParticipant(call: ApplicationCall)
    suspend fun getEventsAsOrganizer(call: ApplicationCall)
    suspend fun getEventsAsParticipant(call: ApplicationCall)
}