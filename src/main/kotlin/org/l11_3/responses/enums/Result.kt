package org.l11_3.responses.enums

import io.ktor.http.*

enum class Result(val message: String, val code: HttpStatusCode) {
    OK(
        "OK", HttpStatusCode.OK
    ),
    Such_User_Already_Exists(
        "Such user already exists", HttpStatusCode.Conflict
    ),
    Such_User_Does_Not_Exist(
        "Such user doesn`t exist", HttpStatusCode.NotFound
    ),
    Incorrect_Password(
        "Incorrect password", HttpStatusCode.Conflict
    )
}