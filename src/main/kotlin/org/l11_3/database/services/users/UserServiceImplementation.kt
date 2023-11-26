package org.l11_3.database.services.users

import org.jetbrains.exposed.sql.*
import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.database.models.UserUpdate
import org.l11_3.database.services.TableService
import org.l11_3.database.tables.Users
import org.l11_3.responses.values.Result

class UserServiceImplementation : TableService(), UserService {
    override suspend fun register(user: UserRegister): Result {
        if (isUserExists(user)) return Result.SuchUserAlreadyExists
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
            ?: return Result.SuchUserDoesNotExist
        return if (user.password.hashCode() == passwordHash) Result.OK
        else Result.IncorrectPassword
    }

    override suspend fun editData(user: UserUpdate, id: UInt): Result {
        databaseQuery {
            Users.update({ Users.id.eq(id) }) {
                if (user.phone != null) it[phone] = user.phone
                if (user.email != null) it[email] = user.email
                if (user.password != null) it[password] = user.password.hashCode()
                if (user.name != null) it[name] = user.name
                if (user.surname != null) it[surname] = user.surname
                if (user.patronymic != null) it[patronymic] = user.patronymic
                if (user.`class` != null) it[`class`] = user.`class`
            }
        }
        return Result.OK
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
        }.singleOrNull()
    } != null

    override suspend fun getUserPassword(phone: String?, email: String?): Int? = databaseQuery {
        Users.select {
            if (phone == null) Users.email.eq(email)
            else Users.phone.eq(phone)
        }.singleOrNull()
    }.let {
        if (it == null) return@let null
        return@let it[Users.password]
    }

    override suspend fun getUserPassword(id: UInt): Int? = databaseQuery {
        Users.select { Users.id.eq(id) }
    }.singleOrNull().let {
        if (it == null) return@let null
        return@let it[Users.password]
    }

    override suspend fun isTokenValid(id: UInt?, password: Int?): Boolean {
        if (id == null || password == null) return false
        return password == getUserPassword(id = id)
    }
}