package org.l11_3.database.tables

import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.l11_3.database.extensions.array

object Events : Table() {
    val id = integer("id").autoIncrement()
    val organizers = array<Int>("organizers", IntegerColumnType())
    val name = text("name")
    val start = long("start")
    val end = long("end")
    val description = text("description")
    val picture = binary("picture")
    val participants = array<Int>("participants", IntegerColumnType())
    override val primaryKey = PrimaryKey(id)
}