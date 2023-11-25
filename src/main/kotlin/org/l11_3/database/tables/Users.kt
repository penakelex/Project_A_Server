package org.l11_3.database.tables

import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.l11_3.database.extensions.array

object Users : Table() {
    val id = integer("id").autoIncrement()
    val phone = text("phone")
    val email = text("email")
    val password = integer("password")
    val name = text("name")
    val surname = text("surname")
    val patronymic = text("patronymic")
    val `class` = byte("class")
    val events = array<Int>("events", IntegerColumnType())
    val events_participant = array<Int>("events_participant", IntegerColumnType())
    override val primaryKey = PrimaryKey(Events.id)
}