package org.l11_3.di

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.l11_3.database.Service
import org.l11_3.database.services.users.UserServiceImplementation

val mainModule = module {
    single {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/psasic-general",
            user = "postgres",
            password = "p@ssw0rd",
            driver = "org.postgresql.Driver"
        )
    }
    single {
        Service(
            database = get(),
            userService = UserServiceImplementation()
        )
    }
}