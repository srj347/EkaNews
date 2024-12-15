package com.example.core.data.repository

import android.util.Log
import com.example.core.database.dao.ArticleDao
import com.example.core.database.entity.asExternalModel
import com.example.core.network.api.NewsApi
import com.example.core.network.model.NewsResponse
import com.example.core.network.model.asEntity
import com.example.core.network.model.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRemoteRepository(
    private val api: NewsApi,
    private val db: ArticleDao
) {
    suspend fun getBreakingNews() = withContext(Dispatchers.IO) {
        /**
         * Fetching the articles from remote
         */
        var response: NewsResponse? = null
        try {
            response = api.getBreakingNews()
        } catch (e: Exception) {
            Log.d(TAG, "Failed to fetch articles from remote => ${e.message}")
        }

        /**
         * Saving articles to local storage (single source of truth)
         */
        response?.articles.let { articles ->
            articles?.forEach { article ->
                db.upsertArticle(article.asEntity())
            }
        }

        /**
         * Fetching article from local storage
         */
        val result = db.getAllArticles().map {
            it.asExternalModel()
        }

        return@withContext result
    }

    suspend fun searchNews(searchQuery:String) = withContext(Dispatchers.IO){
        val response = api.searchNews(searchQuery = searchQuery)
        return@withContext response.articles.map {
            it.asExternalModel()
        }
    }

    companion object {
        val TAG = NewsRemoteRepository::class.simpleName
    }
}