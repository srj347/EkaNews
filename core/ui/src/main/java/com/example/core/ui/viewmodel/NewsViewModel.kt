package com.example.core.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.NewsLocalRepository
import com.example.core.data.repository.NewsRemoteRepository
import com.example.core.model.Article
import com.example.core.ui.navigation.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NewsViewModel(
    private val remoteRepository: NewsRemoteRepository,
    private val localRepository: NewsLocalRepository
) : ViewModel() {

    private val _headlineUiState: MutableLiveData<UiState> = MutableLiveData()
    val headlineUiState: LiveData<UiState> = _headlineUiState

    private val _searchUiState: MutableLiveData<UiState> = MutableLiveData()
    val searchUiState: LiveData<UiState> = _searchUiState

    private val _savedUiState: MutableLiveData<UiState> = MutableLiveData()
    val savedUiState: LiveData<UiState> = _savedUiState

    var searchedText: String = ""

    private var searchJob: Job? = null

    fun getBreakingNews() {
        viewModelScope.launch {
            _headlineUiState.value = UiState.Loading
            try {
                val response = remoteRepository.getBreakingNews()
                _headlineUiState.value = UiState.Success(response)
            } catch (e: Exception) {
                _headlineUiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getSavedArticles(){
        viewModelScope.launch {
            try {
                val savedArticles = localRepository.getSavedArticles()
                _savedUiState.value = UiState.Success(savedArticles)
            } catch (e: Exception) {
                _savedUiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            try {
                localRepository.saveArticle(article)
                val savedArticles = localRepository.getSavedArticles()
                _savedUiState.value = UiState.Success(savedArticles)
            } catch (e: Exception) {
                _savedUiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun unSaveArticle(article: Article) {
        viewModelScope.launch {
            try {
                localRepository.unSaveArticle(article)
                val savedArticles = localRepository.getSavedArticles()
                _savedUiState.value = UiState.Success(savedArticles)
            } catch (e: Exception) {
                _savedUiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun searchNews(searchQuery: String) {
        if (searchQuery.isEmpty() || searchQuery.length <= 2) {
            // If the search query is invalid, return an empty list and update UI state
            _searchUiState.value = UiState.Success(emptyList())
            return
        }

        /**
         * Performing search for search query greater than 2 characters
         */
        searchJob = viewModelScope.launch {
            delay(500) // Debouncing effect
            try {
                _searchUiState.value = UiState.Loading
                val searchedNews = remoteRepository.searchNews(searchQuery)
                _searchUiState.value = UiState.Success(searchedNews)
            } catch (e: Exception) {
                _searchUiState.value = UiState.Error(e.message.toString())
            }
        }
    }

}


