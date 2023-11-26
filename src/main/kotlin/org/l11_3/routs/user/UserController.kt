package org.l11_3.routs.user

import io.ktor.server.application.*

interface UserController {
    suspend fun register(call: ApplicationCall)
    suspend fun login(call: ApplicationCall)
    suspend fun editData(call: ApplicationCall)
}