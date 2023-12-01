package org.l11_3.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.l11_3.routes.events.EventsController
import org.l11_3.routes.events.events
import org.l11_3.routes.user.UserController
import org.l11_3.routes.user.user

fun Application.configureRouting() {
    val userController by inject<UserController>()
    val eventsController by inject<EventsController>()
    routing {
        user(userController)
        events(eventsController)
    }
}
