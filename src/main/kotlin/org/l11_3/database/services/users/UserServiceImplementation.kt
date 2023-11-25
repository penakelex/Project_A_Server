package org.l11_3.database.services.users

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.database.services.TableService
import org.l11_3.database.tables.Users
import org.l11_3.responses.enums.Result

class UserServiceImplementation : TableService(), UserService {
    override suspend fun register(user: UserRegister): Result {
        if (isUserExists(user)) return Result.Such_User_Already_Exists
        databaseQuery {
            Users.insert {
                it[phone] = user.phone
                it[email] = user.email
                it[password] = user.password.hashCode()
                it[name] = user.name
                it[surname] = user.surname
                it[patronymic] = user.patronymic
                it[`class`] = user.`class`
            }
        }
        return Result.OK
    }

    override suspend fun login(user: UserLogin): Result {
        val passwordHash = getUserPassword(phone = user.phone, email = user.email)
            ?: return Result.Such_User_Does_Not_Exist
        return if (user.password.hashCode() == passwordHash) Result.OK
        else Result.Incorrect_Password
    }

    override suspend fun isUserExists(user: UserRegister): Boolean = databaseQuery {
        Users.select {
            (if (user.phone == null) Users.email.eq(user.email)
            else Users.phone.eq(user.phone))
                .or(
                    (Users.name.eq(user.name))
                        .and(Users.surname.eq(user.surname))
                        .and(Users.patronymic.eq(user.patronymic))
                        .and(Users.`class`.eq(user.`class`))
                )
        }
    }.singleOrNull() != null

    override suspend fun getUserPassword(phone: String?, email: String?): Int? = databaseQuery {
        Users.select {
            if (phone == null) Users.email.eq(email)
            else Users.phone.eq(phone)
        }
    }.singleOrNull().let {
        if (it == null) return@let null
        return@let it[Users.password]
    }
}