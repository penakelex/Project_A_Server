package org.l11_3.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.l11_3.di.mainModule

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(mainModule)
    }
}