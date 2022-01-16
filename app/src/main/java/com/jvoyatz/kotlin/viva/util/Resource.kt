package com.jvoyatz.kotlin.viva.util

sealed class Resource<T>(var data: T? = null, val message: String? = null) {
    class Success<T>(data:T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(): Resource<T>()
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object InProgress : Result<Nothing>()
}


//companion object{
//    fun resolveError(e: Exception): State.ErrorState {
//        var error = e
//
//        when (e) {
//            is SocketTimeoutException -> {
//                error = NetworkErrorException(errorMessage = "connection error!")
//            }
//            is ConnectException -> {
//                error = NetworkErrorException(errorMessage = "no internet access!")
//            }
//            is UnknownHostException -> {
//                error = NetworkErrorException(errorMessage = "no internet access!")
//            }
//        }
//
//        if(e is HttpException){
//            when(e.code()){
//                502 -> {
//                    error = NetworkErrorException(e.code(),  "internal error!")
//                }
//                401 -> {
//                    throw AuthenticationException("authentication error!")
//                }
//                400 -> {
//                    error = NetworkErrorException.parseException(e)
//                }
//            }
//        }
//
//
//        return State.ErrorState(error)
//    }
//}