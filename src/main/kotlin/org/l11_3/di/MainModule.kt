package org.l11_3.di

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.l11_3.database.Service
import org.l11_3.database.services.users.UserServiceImplementation
import org.l11_3.routs.user.UserController
import org.l11_3.routs.user.UserControllerImplementation

val mainModule = module {
    single {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/project",
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
    single<UserController> { UserControllerImplementation(service = get()) }
}