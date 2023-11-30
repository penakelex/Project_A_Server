package org.l11_3.database.services.events

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.l11_3.database.extensions.minus
import org.l11_3.database.models.EventCreate
import org.l11_3.database.models.EventEdit
import org.l11_3.database.models.EventToDelete
import org.l11_3.database.services.TableService
import org.l11_3.database.tables.Events
import org.l11_3.responses.values.Result

class EventsServiceImplementation : TableService(), EventsService {
    override suspend fun create(event: EventCreate): Result {
        Events.insert {
            it[organizers] = event.organizers
            it[name] = event.name
            it[start] = event.start
            it[end] = event.end
            it[description] = event.description
            it[picture] = event.picture
            it[participants] = event.participants ?: arrayOf()
        }
        return Result.OK
    }

    override suspend fun editData(event: EventEdit, userID: UInt): Result = databaseQuery {
        val organizers = Events.select { Events.id.eq(event.id) }
            .singleOrNull().let {
                if (it == null) return@let null
                it[Events.organizers]
            } ?: return@databaseQuery Result.NoEventWithSuchID
        if (userID !in organizers) {
            return@databaseQuery Result.UCanNotManageThisEvent
        }
        Events.update({ Events.id.eq(event.id) }) {
            if (event.organizers != null) it[Events.organizers] = event.organizers
            if (event.name != null) it[name] = event.name
            if (event.start != null) it[start] = event.start
            if (event.end != null) it[end] = event.end
            if (event.description != null) it[description] = event.description
            if (event.picture != null) it[picture] = event.picture
        }
        Result.OK
    }


    override suspend fun delete(eventID: UInt, userID: UInt): Pair<Result, EventToDelete?> = databaseQuery {
        val eventToDelete = Events.select { Events.id.eq(eventID) }.singleOrNull().let {
            if (it == null) return@let null
            EventToDelete(
                organizers = it[Events.organizers],
                bobbleheads = it[Events.bobbleheads],
                participants = it[Events.participants]
            )
        } ?: return@databaseQuery Result.NoEventWithSuchID to null
        if (userID !in eventToDelete.organizers) {
            return@databaseQuery Result.UCanNotManageThisEvent to null
        }
        Events.deleteWhere { id.eq(eventID) }
        Result.OK to eventToDelete
    }

    override suspend fun addParticipant(eventID: UInt, participantID: UInt): Result = databaseQuery {
        val participants = Events.select { Events.id.eq(eventID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Events.participants]
        } ?: return@databaseQuery Result.NoEventWithSuchID
        if (participantID in participants) return@databaseQuery Result.UAlreadyParticipantOfTheEvent
        Events.update({ Events.id.eq(eventID) }) {
            it[Events.participants] = participants.plus(participantID)
        }
        Result.OK
    }

    override suspend fun removeParticipant(eventID: UInt, participantID: UInt): Result = databaseQuery {
        val participants = Events.select { Events.id.eq(eventID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Events.participants]
        } ?: return@databaseQuery Result.NoEventWithSuchID
        if (participantID !in participants) return@databaseQuery Result.UAreNotParticipantOfThisEvent
        Events.update({ Events.id.eq(eventID) }) {
            it[Events.participants] = participants.minus(participantID)
        }
        Result.OK
    }

    override suspend fun getEvents(
        eventsIDs: List<UInt>
    ): Pair<Result, List<org.l11_3.database.models.Events>> = Result.OK to databaseQuery {
        buildList {
            for (eventID in eventsIDs) {
                Events.select { Events.id.eq(eventID) }.singleOrNull().let {
                    if (it != null) {
                        add(
                            org.l11_3.database.models.Events(
                                organizers = it[Events.organizers],
                                name = it[Events.name],
                                start = it[Events.start],
                                end = it[Events.end],
                                description = it[Events.description],
                                picture = it[Events.picture]
                            )
                        )
                    }
                }
            }
        }
    }
}