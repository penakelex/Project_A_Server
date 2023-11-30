package org.l11_3.database.services

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.l11_3.database.services.events.EventsService
import org.l11_3.database.services.users.UsersService
import org.l11_3.database.tables.Events
import org.l11_3.database.tables.Users

class Service(
    database: Database,
    val usersService: UsersService,
    val eventsService: EventsService
) {
    init {
        transaction(database) {
            SchemaUtils.createSchema()
            SchemaUtils.create(Users, Events)
        }
    }
}