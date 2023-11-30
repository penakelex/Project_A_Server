package org.l11_3.responses.models

import kotlinx.serialization.Serializable

@Serializable
data class Response<out Type>(
    val result: ResultResponse,
    val data: Type? = null
)

@Serializable
data class ResultResponse(
    val statusCode: Short,
    val message: String
)