package org.l11_3.database.services.events

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.l11_3.database.models.EventCreate
import org.l11_3.database.models.EventEdit
import org.l11_3.database.models.EventToDelete
import org.l11_3.database.services.TableService
import org.l11_3.database.tables.Events
import org.l11_3.responses.values.Result
import kotlin.math.E

class EventsServiceImplementation : TableService(), EventsService {
    override suspend fun create(event: EventCreate): Result {
        Events.insert {
            it[organizers] = event.organizers
            it[presenters] = event.presenters
            it[name] = event.name
            it[start] = event.start
            it[end] = event.end
            it[description] = event.description
            it[picture] = event.picture
            it[participants] = event.participants ?: arrayOf()
        }
        return Result.OK
    }

    override suspend fun editData(event: EventEdit): Result = databaseQuery {
        val organizers = Events.select { Events.id.eq(event.id) }
            .singleOrNull().let {
                if (it == null) return@let null
                it[Events.organizers]
            } ?: return@databaseQuery Result.NoEventWithSuchID
        if (event.userID !in organizers) {
            return@databaseQuery Result.UCanNotManageThisEvent
        }
        Events.update({ Events.id.eq(event.id) }) {
            if (event.organizers != null) it[Events.organizers] = event.organizers
            if (event.presenters != null) it[presenters] = event.presenters
            if (event.name != null) it[name] = event.name
            if (event.start != null) it[start] = event.start
            if (event.end != null) it[end] = event.end
            if (event.description != null) it[description] = event.description
            if (event.picture != null) it[picture] = event.picture
        }
        return@databaseQuery Result.OK
    }


    override suspend fun delete(id: UInt, userID: UInt): Result = databaseQuery {
        val eventToDelete = Events.select { Events.id.eq(id) }.singleOrNull().let {
            if (it == null) return@let null
            EventToDelete(
                organizers = it[Events.organizers],
                presenters = it[Events.presenters],
                participants = it[Events.participants]
            )
        } ?: return@databaseQuery Result.NoEventWithSuchID
        if (userID !in eventToDelete.organizers) {
            return@databaseQuery Result.UCanNotManageThisEvent
        }
        Events.deleteWhere { Events.id.eq(id) }
        return@databaseQuery Result.OK
    }
}