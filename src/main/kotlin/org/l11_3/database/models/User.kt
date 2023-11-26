package org.l11_3.database.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val code: String,
    val phone: String?,
    val email: String?,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val `class`: Byte?
)

@Serializable
data class UserLogin(
    val phone: String?,
    val email: String?,
    val password: String
)
