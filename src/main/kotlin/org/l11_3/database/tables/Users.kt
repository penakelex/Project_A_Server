package org.l11_3.database.tables

import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.l11_3.database.extensions.array

object Users : Table() {
    val id = uinteger("id").autoIncrement()
    val phone = text("phone").nullable()
    val email = text("email").nullable()
    val password = integer("password")
    val name = text("name")
    val surname = text("surname")
    val patronymic = text("patronymic")
    val `class` = ubyte("class").nullable()
    val events = array<UInt>("events", IntegerColumnType()).default(Array(0) { 0.toUInt() })
    val events_participant = array<UInt>("events_participant", IntegerColumnType())
        .default(Array(0) { 0.toUInt() })
    override val primaryKey = PrimaryKey(Events.id)
}