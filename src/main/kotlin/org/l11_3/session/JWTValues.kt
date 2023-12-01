package org.l11_3.session

data class JWTValues(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)