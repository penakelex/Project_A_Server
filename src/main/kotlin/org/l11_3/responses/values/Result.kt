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
    ),
    UAlreadyParticipantOfTheEvent(
        "You already participant of the event", HttpStatusCode.Conflict
    ),
    UAreNotParticipantOfThisEvent(
        "You aren`t participant of this event", HttpStatusCode.NotFound
    ),
    UWasNotOrganizerOfThisEvent(
        "You wasn`t organizer of this event", HttpStatusCode.NotFound
    ),
    UWasNotParticipantOfThisEvent(
        "You wasn`t participant of this event", HttpStatusCode.NotFound
    ),
    UCanNotManageThisUser(
        "You can`t manage this user", HttpStatusCode.Forbidden
    ),
    UAlreadyQuited(
        "You already quited", HttpStatusCode.Conflict
    ),
    NewPasswordSameAsLast(
        "New password same as last", HttpStatusCode.Found
    ),
    NewEmailSameAsLast(
        "New email same as last", HttpStatusCode.Found
    ),
    UAlreadyOrganizerOfThisEvent(
        "You already organizer of this event", HttpStatusCode.Conflict
    )
}