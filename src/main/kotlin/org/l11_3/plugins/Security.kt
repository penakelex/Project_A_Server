package org.l11_3.plugins

import com.auth0.jwt.JWT
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import org.l11_3.database.Service
import org.l11_3.responses.values.Result
import org.l11_3.session.JWTValues
import org.l11_3.session.getAlgorithm
import org.l11_3.session.getUserIDString

fun Application.configureSecurity(values: JWTValues) {
    val service by inject<Service>()
    authentication {
        jwt {
            realm = values.realm
            verifier(
                JWT
                    .require(getAlgorithm(secret = values.secret))
                    .withAudience(values.audience)
                    .withIssuer(values.issuer)
                    .build()
            )
            validate { jwtCredential ->
                val isTokenValid = service.userService.isTokenValid(
                    id = jwtCredential.payload.getClaim(getUserIDString())?.asInt(),
                    password = jwtCredential.payload.getClaim(getUserIDString())?.asInt()
                )
                val isNotExpired = (jwtCredential.expiresAt?.time ?: Long.MIN_VALUE) > System.currentTimeMillis()
                if (isTokenValid && isNotExpired) JWTPrincipal(jwtCredential.payload)
                else null
            }
            challenge { _, _ -> call.respond(Result.TokenIsNotValidOrExpired) }
        }
    }
}

fun Application.getValues(): JWTValues {
    val config = HoconApplicationConfig(ConfigFactory.load())
    return JWTValues(
        secret = config.property("jwt.secret").getString(),
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        realm = config.property("jwt.realm").getString()
    )
}
