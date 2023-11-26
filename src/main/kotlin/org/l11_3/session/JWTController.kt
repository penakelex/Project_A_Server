package org.l11_3.session

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

private const val userID = "userID"
private const val password = "password"
private const val ONE_YEAR_IN_MS = 31_536_000_000

fun generateToken(values: JWTValues, id: Int, userPassword: Int): String = JWT
    .create()
    .withAudience(values.audience)
    .withIssuer(values.issuer)
    .withClaim(userID, id)
    .withClaim(password, userPassword)
    .withExpiresAt(Date(System.currentTimeMillis() + ONE_YEAR_IN_MS))
    .sign(getAlgorithm(values.secret))

/**
 * Returns "userID"
 * @return userID: String
 * */
fun getUserIDString() = userID

/**
 * Returns "password"
 * @return password: String
 * */
fun getPasswordString() = password

fun getAlgorithm(secret: String): Algorithm = Algorithm.HMAC512(secret)