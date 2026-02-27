package com.androidstoreapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getUser: GetUserUseCase,
    private val getFavorites: GetFavoritesUseCase
) : ViewModel() {

    data class ProfileUiState(
        val userState: UiState<User> = UiState.Loading,
        val favoriteCount: Int = 0
    )

    private val refreshSignal = MutableStateFlow(Unit)

    val uiState: StateFlow<ProfileUiState> = refreshSignal.flatMapLatest {
        combine(getUser(), getFavorites()) { userResult, favIds ->
            ProfileUiState(
                userState = userResult.fold(
                    onSuccess = { UiState.Content(it) },
                    onFailure = { UiState.Error(it) }
                ),
                favoriteCount = favIds.size
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ProfileUiState())

    fun load() {
        refreshSignal.value = Unit
    }
}
