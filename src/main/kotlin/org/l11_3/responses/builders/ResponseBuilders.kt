package org.l11_3.responses.builders

import org.l11_3.responses.models.Response
import org.l11_3.responses.models.ResultResponse
import org.l11_3.responses.values.Result

fun Result.toResultResponse() = ResultResponse(code.value.toShort(), message)

fun <Type> Pair<Result, Type?>.toResponse() = Response(
    result = first.toResultResponse(),
    data = second
)