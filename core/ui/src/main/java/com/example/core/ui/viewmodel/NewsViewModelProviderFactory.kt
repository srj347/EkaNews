package com.example.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.repository.NewsLocalRepository
import com.example.core.data.repository.NewsRemoteRepository

class NewsViewModelProviderFactory(
    private val remoteRepository: NewsRemoteRepository,
    private val localRepository: NewsLocalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(remoteRepository, localRepository) as T
    }
}