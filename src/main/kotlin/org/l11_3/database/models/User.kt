package org.l11_3.database.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val phone: String?,
    val email: String?,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val status: String
)

@Serializable
data class UserLogin(
    val phone: String?,
    val email: String?,
    val password: String
)

@Serializable
data class UserUpdate(
    val phone: String?,
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val status: String?
)

@Serializable
data class User(
    val phone: String?,
    val email: String?,
    val name: String,
    val surname: String,
    val patronymic: String,
    val status: String
)

@Serializable
data class Code(
    val type: String,
    val eventID: UInt?,
    val eventState: String?
)

data class UserQuit(
    val password: Int,
    val entered: Boolean
)

data class UserSecurity(
    val id: UInt,
    val password: Int
)