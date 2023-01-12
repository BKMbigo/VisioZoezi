package com.github.bkmbigo.visiozoezi.common.data.network.utils

sealed class NetworkResult<T>{
    data class Success<T>(val data: T): NetworkResult<T>()
    data class Error<T>(val errorCode: Int, val errorMessage: String): NetworkResult<T>()
}