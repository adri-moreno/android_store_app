package com.androidstoreapp.di

import androidx.room.Room
import com.androidstoreapp.core.database.AppDatabase
import com.androidstoreapp.core.database.FavoriteLocalDataSourceImpl
import com.androidstoreapp.core.network.AndroidNetworkMonitor
import com.androidstoreapp.core.network.NetworkMonitor
import com.androidstoreapp.core.network.provideApiService
import com.androidstoreapp.core.network.provideOkHttpClient
import com.androidstoreapp.core.network.repository.ProductRepositoryImpl
import com.androidstoreapp.core.network.repository.UserRepositoryImpl
import com.androidstoreapp.domain.datasource.FavoriteLocalDataSource
import com.androidstoreapp.domain.repository.ProductRepository
import com.androidstoreapp.domain.repository.UserRepository
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetProductsUseCase
import com.androidstoreapp.domain.usecase.GetUserUseCase
import com.androidstoreapp.domain.usecase.ObserveProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import com.androidstoreapp.feature.favorites.FavoritesViewModel
import com.androidstoreapp.feature.products.ProductsViewModel
import com.androidstoreapp.feature.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Network
    single<NetworkMonitor> { AndroidNetworkMonitor(get()) }
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "store_db"
        ).build()
    }
    single { get<AppDatabase>().favoriteDao() }
    single<FavoriteLocalDataSource> { FavoriteLocalDataSourceImpl(get()) }

    // Repositories
    single<ProductRepository> { ProductRepositoryImpl(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetProductsUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { GetUserUseCase(get()) }
    factory { ObserveProductsUseCase(get()) }

    // ViewModels
    viewModel { ProductsViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}
