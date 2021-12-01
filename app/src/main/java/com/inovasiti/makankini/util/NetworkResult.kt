package com.inovasiti.makankini.util

//Sealed class (like enum + other features) = has flexibility of diff types of subclasses, using generic class & contain the state.
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}
