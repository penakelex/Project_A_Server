package org.l11_3.database.tables

import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.l11_3.database.extensions.array

object Users : Table() {
    val id = uinteger("id").autoIncrement()
    val phone = varchar("phone", 15).nullable()
    val email = text("email").nullable()
    val password = integer("password")
    val name = varchar("name", 20)
    val surname = text("surname")
    val patronymic = varchar("patronymic", 30)
    val status = varchar("class", 20)
    val events = array<Int>("events", IntegerColumnType()).default(arrayOf())
    val events_participant = array<Int>("events_participant", IntegerColumnType())
        .default(arrayOf())
    val events_bobblehead = array<Int>("events_bobblehead", IntegerColumnType())
        .default(arrayOf())
    val entered = bool("entered").default(true)
    override val primaryKey = PrimaryKey(Events.id)
}