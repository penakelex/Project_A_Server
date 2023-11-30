package org.l11_3.di

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.l11_3.database.services.Service
import org.l11_3.database.services.events.EventsServiceImplementation
import org.l11_3.database.services.users.UsersServiceImplementation
import org.l11_3.routes.events.EventsController
import org.l11_3.routes.events.EventsControllerImplementation
import org.l11_3.routes.user.UserController
import org.l11_3.routes.user.UserControllerImplementation
import org.l11_3.session.JWTValues

val mainModule = module {
    val config = HoconApplicationConfig(ConfigFactory.load())
    single {
        Database.connect(
            url = config.property("database.url").getString(),
            user = config.property("database.user").getString(),
            password = config.property("database.password").getString(),
            driver = config.property("database.driver").getString()
        )
    }
    single {
        Service(
            database = get(),
            usersService = UsersServiceImplementation(),
            eventsService = EventsServiceImplementation()
        )
    }
    single<JWTValues> {
        JWTValues(
            secret = config.property("jwt.secret").getString(),
            issuer = config.property("jwt.issuer").getString(),
            audience = config.property("jwt.audience").getString(),
            realm = config.property("jwt.realm").getString()
        )
    }
    single<UserController> { UserControllerImplementation(service = get(), values = get()) }
    single<EventsController> { EventsControllerImplementation(service = get()) }

}