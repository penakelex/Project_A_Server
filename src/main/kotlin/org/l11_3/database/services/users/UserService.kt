package org.l11_3.database.services.users

import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.database.models.UserUpdate
import org.l11_3.responses.values.Result

interface UserService {
    suspend fun register(user: UserRegister): Result
    suspend fun login(user: UserLogin): Result
    suspend fun editData(user: UserUpdate, id: UInt): Result
    suspend fun getUserPassword(phone: String?, email: String?): Int?
    suspend fun isUserExists(user: UserRegister): Boolean
    suspend fun getUserPassword(id: UInt): Int?
    suspend fun isTokenValid(id: UInt?, password: Int?): Boolean

}