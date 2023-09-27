package com.mshaw.data.util.extensions

import kotlin.Exception

fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}