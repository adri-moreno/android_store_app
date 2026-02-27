package com.androidstoreapp.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(networkMonitor: NetworkMonitor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(ConnectivityInterceptor(networkMonitor))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

fun provideApiService(client: OkHttpClient): ApiService =
    Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
