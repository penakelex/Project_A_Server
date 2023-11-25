package org.l11_3.di

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val module = module {
    single {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/psasic-general",
            user = "postgres",
            password = "p@ssw0rd",
            driver = "org.postgresql.Driver"
        )
    }
}