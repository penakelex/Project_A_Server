package org.l11_3.database.models

data class UserRegister(
    val phone: String?,
    val email: String?,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val `class`: Byte?
)

data class UserLogin(
    val phone: String?,
    val email: String?,
    val password: String
)

data class UserCheck(
    val login: String,
    val password: Int
)

data class UserSecurity(
    val id: Int,
    val password: Int
)