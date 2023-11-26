package org.l11_3

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.l11_3.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val values = getValues()
    configureSerialization()
    configureSecurity(values)
    configureRouting()
}
