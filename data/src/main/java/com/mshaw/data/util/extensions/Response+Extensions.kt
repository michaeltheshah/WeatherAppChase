package com.mshaw.data.util.extensions

import com.mshaw.data.model.ErrorResponse
import com.mshaw.data.util.state.AwaitResult
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response

fun <T> Response<T>.awaitResult(): AwaitResult<T> {
    return try {
        if (isSuccessful) {
            val body = body()
            if (body != null) {
                AwaitResult.Ok(body, raw())
            } else {
                AwaitResult.Error(NullPointerException("Response body is null"), raw(), errorBody()?.string())
            }
        } else {
            AwaitResult.Error(HttpException(this), raw(), errorBody()?.string())
        }
    } catch (e: Exception) {
        val json = errorBody()?.string()
        val errorResponse = tryOrNull { Json.decodeFromString<ErrorResponse>(json!!) }
        AwaitResult.Error(e, raw(), errorBody()?.string(), errorResponse)
    }
}

val <T> Response<T>.value: T
    get() = try {
        body() as T
    } catch (e: Exception) {
        throw HttpException(this)
    }