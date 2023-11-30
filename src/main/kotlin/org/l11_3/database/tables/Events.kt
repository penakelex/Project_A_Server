package org.l11_3.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.TextColumnType
import org.jetbrains.exposed.sql.UIntegerColumnType
import org.l11_3.database.extensions.array

object Events : Table() {
    val id = uinteger("id").autoIncrement()
    val organizers = array<UInt>("organizers", UIntegerColumnType())
    val bobbleheads = array<UInt>("presenters", TextColumnType())
        .default(arrayOf())
    val name = text("name")
    val start = ulong("start")
    val end = ulong("end")
    val description = text("description")
    val picture = text("picture")
    val participants = array<UInt>("participants", UIntegerColumnType())
        .default(arrayOf())
    override val primaryKey = PrimaryKey(id)
}