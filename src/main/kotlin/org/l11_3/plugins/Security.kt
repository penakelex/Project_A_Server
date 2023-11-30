package org.l11_3.plugins

import com.auth0.jwt.JWT
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import org.l11_3.database.services.Service
import org.l11_3.responses.values.Result
import org.l11_3.session.JWTValues
import org.l11_3.session.getAlgorithm
import org.l11_3.session.getPasswordString
import org.l11_3.session.getUserIDString

fun Application.configureSecurity() {
    val service by inject<Service>()
    val values by inject<JWTValues>()
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
                val isTokenValid = service.usersService.isTokenValid(
                    id = jwtCredential.payload.getClaim(getUserIDString()).asInt()?.toUInt(),
                    password = jwtCredential.payload.getClaim(getPasswordString())?.asInt(),
                )
                val isNotExpired = (jwtCredential.expiresAt?.time ?: Long.MIN_VALUE) > System.currentTimeMillis()
                if (isTokenValid && isNotExpired) JWTPrincipal(jwtCredential.payload)
                else null
            }
            challenge { _, _ -> call.respond(Result.TokenIsNotValidOrExpired) }
        }
    }
}

