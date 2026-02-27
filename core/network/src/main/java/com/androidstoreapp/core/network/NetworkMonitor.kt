package com.androidstoreapp.core.network

import kotlinx.coroutines.flow.Flow
import java.io.IOException

class NoInternetException : IOException("No internet connection")

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    fun isConnected(): Boolean
}
