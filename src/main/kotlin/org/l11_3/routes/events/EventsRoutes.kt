package org.l11_3.routes.events

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.events(eventsController: EventsController) {
    post("event/create") { eventsController.create(call) }
    authenticate {
        post("event/edit") { eventsController.editData(call) }

        post("event/delete") { eventsController.delete(call) }

        post("event/addParticipant") {
            eventsController.addParticipant(call)
        }

        post("event/removeParticipant") {
            eventsController.removeParticipant(call)
        }

        post("event/getEventsAsOrganizer") {
            eventsController.getEventsAsOrganizer(call)
        }

        post("event/getEventsAsParticipant") {
            eventsController.getEventsAsParticipant(call)
        }
    }
}