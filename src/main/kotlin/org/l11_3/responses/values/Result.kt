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
    ),
    NoEventWithSuchID(
        "No event with such ID", HttpStatusCode.NotFound
    ),
    UCanNotManageThisEvent(
        "You can`t manage this event", HttpStatusCode.Forbidden
    )
}