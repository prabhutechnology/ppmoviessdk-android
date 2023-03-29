package com.prabhutech.ppmoviessdk.core.utils

sealed class Resource<T>(val data: T?, val errorTitle: UiText?, val message: UiText?) {
    class Success<T>(data: T) : Resource<T>(data, null, null)
    class Error<T>(errorTitle: UiText, message: UiText) : Resource<T>(null, errorTitle, message)
}