package org.l11_3.database.services.events

import org.l11_3.database.models.EventCreate
import org.l11_3.responses.values.Result
import org.l11_3.database.models.EventEdit
import org.l11_3.database.models.EventToDelete
import org.l11_3.database.models.Events

interface EventsService {
    suspend fun create(event: EventCreate): Result
    suspend fun editData(event: EventEdit, userID: UInt): Result
    suspend fun delete(eventID: UInt, userID: UInt): Pair<Result, EventToDelete?>
    suspend fun addParticipant(eventID: UInt, participantID: UInt): Result
    suspend fun removeParticipant(eventID: UInt, participantID: UInt): Result
    suspend fun getEvents(eventsIDs: List<UInt>): Pair<Result, List<Events>>
}