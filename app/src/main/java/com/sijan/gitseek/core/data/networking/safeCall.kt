package com.sijan.gitseek.core.data.networking

import com.sijan.gitseek.core.domain.utils.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import com.sijan.gitseek.core.domain.utils.Result

suspend inline fun <reified T> safeApiCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    }catch (e: UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTERNET)
    }
    catch (e: SerializationException){
        return Result.Error(NetworkError.SERIALIZATION)
    }catch (e: Exception){
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}