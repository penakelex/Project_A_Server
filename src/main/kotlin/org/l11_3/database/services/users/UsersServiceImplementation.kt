package org.l11_3.database.services.users

import org.jetbrains.exposed.sql.*
import org.l11_3.database.extensions.minus
import org.l11_3.database.models.*
import org.l11_3.database.services.TableService
import org.l11_3.database.states.EventState
import org.l11_3.database.tables.Users
import org.l11_3.responses.values.Result

class UsersServiceImplementation : TableService(), UsersService {
    override suspend fun register(user: UserRegister): Pair<Result, UserSecurity?> {
        if (isUserExists(user)) return Result.SuchUserAlreadyExists to null
        val userSecurity = databaseQuery {
            Users.insert {
                it[phone] = user.phone
                it[email] = user.email
                it[password] = user.password.hashCode()
                it[name] = user.name
                it[surname] = user.surname
                it[patronymic] = user.patronymic
                it[status] = user.status
            }
            Users.select {
                if (user.phone == null) Users.email.eq(user.email)
                else Users.phone.eq(user.phone)
            }.single()
        }.let {
            UserSecurity(
                id = it[Users.id], password = it[Users.password]
            )
        }
        return Result.OK to userSecurity
    }

    override suspend fun login(user: UserLogin): Pair<Result, UserSecurity?> {
        val userSecurity = getUserSecurity(phone = user.phone, email = user.email)
            ?: return Result.SuchUserDoesNotExist to null
        if (user.password.hashCode() != userSecurity.password) return Result.IncorrectPassword to null
        databaseQuery {
            Users.update({ Users.id.eq(userSecurity.id) }) {
                it[entered] = true
            }
        }
        return Result.OK to userSecurity
    }

    override suspend fun editData(user: UserUpdate, id: UInt): Result {
        databaseQuery {
            Users.update({ Users.id.eq(id) }) {
                if (user.phone != null) it[phone] = user.phone
                if (user.name != null) it[name] = user.name
                if (user.surname != null) it[surname] = user.surname
                if (user.patronymic != null) it[patronymic] = user.patronymic
                if (user.status != null) it[status] = user.status
            }
        }
        return Result.OK
    }

    override suspend fun updateEmail(newEmail: String, id: UInt): Result = databaseQuery {
        val lastEmail = Users.select { Users.id.eq(id) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.email]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (lastEmail == newEmail) return@databaseQuery Result.NewEmailSameAsLast
        Users.update({ Users.id.eq(id) }) {
            it[email] = newEmail
        }
        return@databaseQuery Result.OK
    }


    override suspend fun updatePassword(newPassword: String, id: UInt, password: Int): Result {
        if (password == newPassword.hashCode()) return Result.NewPasswordSameAsLast
        return databaseQuery {
            val passwordDatabase = Users.select { Users.id.eq(id) }.singleOrNull().let {
                if (it == null) return@let null
                it[Users.password]
            }
            if (passwordDatabase != password.hashCode()) return@databaseQuery Result.UCanNotManageThisUser
            Users.update({ Users.id.eq(id) }) { it[Users.password] = newPassword.hashCode() }
            Result.OK
        }
    }

    override suspend fun deleteEventAsOrganizer(
        userID: UInt, eventID: UInt
    ): Result = databaseQuery {
        val events = Users.select { Users.id.eq(userID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.events]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (eventID.toInt() !in events) return@databaseQuery Result.UWasNotOrganizerOfThisEvent
        Users.update({ Users.id.eq(userID) }) {
            it[Users.events] = events.minus(eventID.toInt())
        }
        return@databaseQuery Result.OK
    }

    override suspend fun deleteEventAsParticipant(
        userID: UInt, eventID: UInt
    ): Result = databaseQuery {
        val eventsParticipant = Users.select { Users.id.eq(userID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.events_participant]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (eventID.toInt() !in eventsParticipant) return@databaseQuery Result.UWasNotParticipantOfThisEvent
        Users.update({ Users.id.eq(userID) }) {
            it[events_participant] = eventsParticipant.minus(eventID.toInt())
        }
        return@databaseQuery Result.OK
    }

    override suspend fun deleteEventAsBobblehead(userID: UInt, eventID: UInt): Result =
        databaseQuery {
            val events = Users.select { Users.id.eq(userID) }.singleOrNull().let {
                if (it == null) return@let null
                it[Users.events_bobblehead]
            } ?: return@databaseQuery Result.SuchUserDoesNotExist
            Users.update({ Users.id.eq(userID) }) {
                it[events_bobblehead] = events.minus(eventID.toInt())
            }
            return@databaseQuery Result.OK
        }


    override suspend fun addEventAsOrganizer(
        userID: UInt, eventID: UInt
    ): Result = databaseQuery {
        val events = Users.select { Users.id.eq(userID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.events]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (eventID.toInt() in events) return@databaseQuery Result.UAlreadyOrganizerOfThisEvent
        Users.update({ Users.id.eq(userID) }) {
            it[Users.events] = events.plus(eventID.toInt())
        }
        Result.OK
    }

    override suspend fun addEventAsParticipant(
        userID: UInt, eventID: UInt
    ): Result = databaseQuery {
        val events = Users.select { Users.id.eq(userID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.events_participant]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (eventID.toInt() in events) return@databaseQuery Result.UAlreadyParticipantOfTheEvent
        Users.update({ Users.id.eq(userID) }) {
            it[events_participant] = events.plus(eventID.toInt())
        }
        Result.OK
    }

    override suspend fun addEventAsBobblehead(userID: UInt, eventID: UInt): Result = databaseQuery {
        val events = Users.select { Users.id.eq(userID) }.singleOrNull().let {
            if (it == null) return@let null
            it[Users.events_participant]
        } ?: return@databaseQuery Result.SuchUserDoesNotExist
        if (eventID.toInt() in events) return@databaseQuery Result.UAlreadyParticipantOfTheEvent
        Users.update({ Users.id.eq(userID) }) {
            it[events_participant] = events.plus(eventID.toInt())
        }
        Result.OK
    }

    override suspend fun getEventsIDsAsOrganizer(userID: UInt): Pair<Result, List<UInt>?> =
        databaseQuery { Users.select { Users.id.eq(userID) }.singleOrNull() }.let {
            if (it == null) Result.SuchUserDoesNotExist to null
            else Result.OK to it[Users.events].map { eventID -> eventID.toUInt() }
        }


    override suspend fun getEventsIDsAsParticipant(userID: UInt): Pair<Result, List<UInt>?> =
        databaseQuery { Users.select { Users.id.eq(userID) }.singleOrNull() }.let {
            if (it == null) Result.SuchUserDoesNotExist to null
            else Result.OK to it[Users.events_participant].map { eventID -> eventID.toUInt() }
        }

    override suspend fun getUserInformation(userID: UInt): Pair<Result, User?> =
        databaseQuery { Users.select { Users.id.eq(userID) }.singleOrNull() }.let {
            if (it == null) return@let Result.SuchUserDoesNotExist to null
            Result.OK to User(
                phone = it[Users.phone],
                email = it[Users.email],
                name = it[Users.name],
                surname = it[Users.surname],
                patronymic = it[Users.patronymic],
                status = it[Users.status]
            )
        }

    override suspend fun checkUserIsRegisteredOnEvent(
        userID: UInt, eventID: UInt, eventState: EventState
    ): Result {
        val events = databaseQuery {
            Users.select { Users.id.eq(userID) }.singleOrNull()
        }.let {
            if (it == null) return@let null
            when (eventState) {
                EventState.Participant -> it[Users.events_participant]
                EventState.Organizer -> it[Users.events]
                else -> it[Users.events_bobblehead]
            }
        } ?: return Result.SuchUserDoesNotExist
        if (eventID.toInt() !in events) return Result.UserDidNotRegisteredOnEvent
        return Result.OK
    }

    override suspend fun checkUserData(
        userID: UInt,
        name: String,
        surname: String,
        patronymic: String,
        status: String
    ): Result {
        val userData = databaseQuery {
            Users.select { Users.id.eq(userID) }.singleOrNull()
        }.let {
            if (it == null) return@let null
            User(
                phone = it[Users.phone],
                email = it[Users.email],
                name = it[Users.name],
                surname = it[Users.surname],
                patronymic = it[Users.patronymic],
                status = it[Users.status]
            )
        } ?: return Result.SuchUserDoesNotExist
        if (userData.name == name && userData.surname == surname
            && userData.patronymic == patronymic && userData.status == status
        ) return Result.OK
        return Result.SuchUserDoesNotExist
    }


    override suspend fun quit(userID: UInt, password: Int): Result {
        val userDatabase = databaseQuery {
            Users.select { Users.id.eq(userID) }.singleOrNull()
        }.let {
            if (it == null) return@let null
            UserQuit(
                password = it[Users.password],
                entered = it[Users.entered]
            )
        } ?: return Result.SuchUserDoesNotExist
        if (userDatabase.password != password) return Result.UCanNotManageThisUser
        if (!userDatabase.entered) return Result.UAlreadyQuited
        Users.update({ Users.id.eq(userID) }) { it[entered] = false }
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
                        .and(Users.status.eq(user.status))
                )
        }.singleOrNull()
    } != null

    override suspend fun getUserSecurity(
        phone: String?, email: String?
    ): UserSecurity? = databaseQuery {
        Users.select {
            if (phone == null) Users.email.eq(email)
            else Users.phone.eq(phone)
        }.singleOrNull()
    }.let {
        if (it == null) return@let null
        UserSecurity(
            id = it[Users.id],
            password = it[Users.password]
        )
    }

    override suspend fun isTokenValid(id: UInt?, password: Int?): Boolean {
        if (id == null || password == null) return false
        val (entered, passwordDatabase) = databaseQuery {
            Users.select { Users.id.eq(id) }.singleOrNull()
        }.let {
            if (it == null) return false
            it[Users.entered] to it[Users.password]
        }
        return entered && passwordDatabase == password
    }
}