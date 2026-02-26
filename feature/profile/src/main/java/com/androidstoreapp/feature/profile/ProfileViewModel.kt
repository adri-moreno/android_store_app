package com.androidstoreapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUser: GetUserUseCase,
    private val getFavorites: GetFavoritesUseCase
) : ViewModel() {

    data class ProfileUiState(
        val userState: UiState<User> = UiState.Loading,
        val favoriteCount: Int = 0
    )

    private val _userResult = MutableStateFlow<Result<User>?>(null)

    val uiState: StateFlow<ProfileUiState> =
        combine(_userResult.filterNotNull(), getFavorites()) { result, favIds ->
            ProfileUiState(
                userState = result.fold(
                    onSuccess = { UiState.Content(it) },
                    onFailure = { UiState.Error(it.message ?: "Error") }
                ),
                favoriteCount = favIds.size
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ProfileUiState())

    init { viewModelScope.launch { _userResult.value = getUser() } }
}
