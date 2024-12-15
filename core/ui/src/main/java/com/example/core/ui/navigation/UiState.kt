package com.example.core.ui.navigation

import com.example.core.model.Article

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val articles: List<Article>? = null
    ) : UiState()

    data class Error(val errorMessage: String) : UiState()
}

inline fun UiState.handle(
    onSuccess: (List<Article>?) -> Unit,
    onLoading: () -> Unit = {},
    onError: (String) -> Unit = {}
) {
    when (this) {
        is UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess(articles)
        is UiState.Error -> onError(errorMessage)
    }
}

suspend fun runCatchingUiState(block: () -> List<Article>?): UiState {
    return try {
        UiState.Success(block())
    } catch (t: Throwable) {
        UiState.Error(t.message ?: "An unknown error occurred")
    }
}
