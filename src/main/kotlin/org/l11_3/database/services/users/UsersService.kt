package org.l11_3.database.services.users

import org.l11_3.database.models.*
import org.l11_3.responses.values.Result

interface UsersService {
    suspend fun register(user: UserRegister): Pair<Result, UserSecurity?>
    suspend fun login(user: UserLogin): Pair<Result, UserSecurity?>
    suspend fun editData(user: UserUpdate, id: UInt): Result
    suspend fun updateEmail(newEmail: String, id: UInt): Result
    suspend fun updatePassword(newPassword: String, id: UInt, password: Int): Result
    suspend fun deleteEventAsOrganizer(userID: UInt, eventID: UInt): Result
    suspend fun deleteEventAsParticipant(userID: UInt, eventID: UInt): Result
    suspend fun deleteEventAsBobblehead(userID: UInt, eventID: UInt): Result
    suspend fun addEventAsOrganizer(userID: UInt, eventID: UInt): Result
    suspend fun addEventAsParticipant(userID: UInt, eventID: UInt): Result
    suspend fun addEventAsBobblehead(userID: UInt, eventID: UInt): Result
    suspend fun getEventsIDsAsOrganizer(userID: UInt): Pair<Result, List<UInt>?>
    suspend fun getEventsIDsAsParticipant(userID: UInt): Pair<Result, List<UInt>?>
    suspend fun getUserInformation(userID: UInt): Pair<Result, User?>
    suspend fun quit(userID: UInt, password: Int): Result
    suspend fun getUserSecurity(phone: String?, email: String?): UserSecurity?
    suspend fun isUserExists(user: UserRegister): Boolean
    suspend fun isTokenValid(id: UInt?, password: Int?): Boolean


}