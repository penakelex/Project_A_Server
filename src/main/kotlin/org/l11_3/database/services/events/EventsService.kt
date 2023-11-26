package org.l11_3.database.services.events

import org.l11_3.database.models.EventCreate
import org.l11_3.responses.values.Result
import org.l11_3.database.models.EventEdit

interface EventsService {
    suspend fun create(event: EventCreate): Result
    suspend fun editData(event: EventEdit): Result
    suspend fun delete(id: UInt, userID: UInt): Result
}