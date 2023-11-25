package org.l11_3.database.services.users

import org.l11_3.database.models.UserLogin
import org.l11_3.database.models.UserRegister
import org.l11_3.responses.values.Result

interface UserService {
    suspend fun register(user: UserRegister): Result
    suspend fun login(user: UserLogin): Result
    suspend fun isUserExists(user: UserRegister): Boolean
    suspend fun getUserPassword(phone: String?, email: String?): Int?
    suspend fun getUserPassword(id: Int): Int?
    suspend fun isTokenValid(id: Int?, password: Int?): Boolean

}