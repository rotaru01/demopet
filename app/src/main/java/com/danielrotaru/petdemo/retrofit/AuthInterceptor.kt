package com.danielrotaru.petdemo.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private val tokenManager = TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {

        if (tokenManager.token.value != null) {

            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${tokenManager.token.value?.accessToken}")
                .build()
            return chain.proceed(modifiedRequest)
        }
        return chain.proceed(chain.request())
    }
}
