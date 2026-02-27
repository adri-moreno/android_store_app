package com.androidstoreapp.core.network

import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val networkMonitor: NetworkMonitor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkMonitor.isConnected()) {
            throw NoInternetException()
        }
        return chain.proceed(chain.request())
    }
}
