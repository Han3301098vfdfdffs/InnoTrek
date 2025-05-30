package com.example.innotrek.ui.utils

sealed class ResultGoogle<out T> {
    data class Success<out T>(val value: T) : ResultGoogle<T>()
    data class Failure(val exception: Exception) : ResultGoogle<Nothing>()
}