package org.l11_3.responses.values

import io.ktor.http.*

enum class Result(val message: String, val code: HttpStatusCode) {
    OK(
        "OK", HttpStatusCode.OK
    ),
    SuchUserAlreadyExists(
        "Such user already exists", HttpStatusCode.Conflict
    ),
    SuchUserDoesNotExist(
        "Such user doesn`t exist", HttpStatusCode.NotFound
    ),
    IncorrectPassword(
        "Incorrect password", HttpStatusCode.Conflict
    ),
    TokenIsNotValidOrExpired(
        "Token isn`t valid or expired", HttpStatusCode.Conflict
    )
}