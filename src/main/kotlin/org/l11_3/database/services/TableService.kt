package org.l11_3.database.services

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

open class TableService {
    suspend fun <Type> databaseQuery(query: suspend () -> Type): Type =
        newSuspendedTransaction(Dispatchers.IO) { query() }
}