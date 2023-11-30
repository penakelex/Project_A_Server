package org.l11_3.routes.events

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.l11_3.database.models.EventCreate
import org.l11_3.database.models.EventEdit
import org.l11_3.database.services.Service
import org.l11_3.responses.builders.toResponse
import org.l11_3.responses.builders.toResultResponse
import org.l11_3.responses.values.Result
import org.l11_3.session.getUserIDString

class EventsControllerImplementation(private val service: Service) : EventsController {
    override suspend fun create(call: ApplicationCall) {
        call.respond(
            service.eventsService.create(call.receive<EventCreate>()).toResultResponse()
        )
    }

    override suspend fun editData(call: ApplicationCall) {
        call.respond(
            service.eventsService.editData(
                event = call.receive<EventEdit>(),
                userID = call.principal<JWTPrincipal>()!!
                    .payload.getClaim(getUserIDString()).asInt().toUInt()
            ).toResultResponse()
        )
    }

    override suspend fun delete(call: ApplicationCall) {
        val eventID = call.receive<UInt>()
        val (deleteResult, eventToDelete) = service.eventsService.delete(
            eventID = eventID,
            userID = call.principal<JWTPrincipal>()!!
                .payload.getClaim(getUserIDString()).asInt().toUInt()
        )
        if (deleteResult != Result.OK) call.respond(deleteResult.toResultResponse())
        for (organizer in eventToDelete!!.organizers) {
            service.usersService.deleteEventAsOrganizer(
                userID = organizer, eventID = eventID
            )
        }
    }

    override suspend fun addParticipant(call: ApplicationCall) {
        val eventID = call.receive<UInt>()
        val participantID = call.principal<JWTPrincipal>()!!
            .payload.getClaim(getUserIDString()).asInt().toUInt()
        val addParticipantResult = service.eventsService.addParticipant(
            eventID = eventID, participantID = participantID
        )
        if (addParticipantResult != Result.OK) call.respond(addParticipantResult.toResultResponse())
        call.respond(
            service.usersService.addEventAsParticipant(
                userID = participantID, eventID = eventID
            ).toResultResponse()
        )
    }

    override suspend fun removeParticipant(call: ApplicationCall) {
        val eventID = call.receive<UInt>()
        val participantID = call.principal<JWTPrincipal>()!!
            .payload.getClaim(getUserIDString()).asInt().toUInt()
        val removeParticipantResult = service.eventsService.removeParticipant(
            eventID = eventID, participantID = participantID
        )
        if (removeParticipantResult != Result.OK) call.respond(removeParticipantResult.toResultResponse())
        call.respond(
            service.usersService.deleteEventAsParticipant(
                userID = participantID, eventID = eventID
            ).toResultResponse()
        )
    }

    override suspend fun getEventsAsOrganizer(call: ApplicationCall) {
        val userID = call.principal<JWTPrincipal>()!!
            .payload.getClaim(getUserIDString()).asInt().toUInt()
        val (getEventsIDsResult, identifiers) = service.usersService.getEventsIDsAsOrganizer(userID)
        if (getEventsIDsResult != Result.OK) call.respond((getEventsIDsResult to null).toResponse())
            .also { return }
        call.respond(
            service.eventsService.getEvents(identifiers!!).toResponse()
        )
    }

    override suspend fun getEventsAsParticipant(call: ApplicationCall) {
        val userID = call.principal<JWTPrincipal>()!!
            .payload.getClaim(getUserIDString()).asInt().toUInt()
        val (getEventsIDsResult, identifiers) = service.usersService.getEventsIDsAsParticipant(userID)
        if (getEventsIDsResult != Result.OK) call.respond((getEventsIDsResult to null).toResponse())
        call.respond(
            service.eventsService.getEvents(identifiers!!).toResponse()
        )
    }
}