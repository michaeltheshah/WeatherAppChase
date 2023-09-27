package com.mshaw.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("cod")
    val code: String?,
    val message: String?
)