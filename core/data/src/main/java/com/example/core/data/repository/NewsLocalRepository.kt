package com.example.core.data.repository

import com.example.core.data.repository.mapper.asEntity
import com.example.core.database.dao.ArticleDao
import com.example.core.database.dao.SavedArticleDao
import com.example.core.database.entity.ArticleEntity
import com.example.core.database.entity.asExternalModel
import com.example.core.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsLocalRepository(
    private val db: SavedArticleDao
) {
    suspend fun saveArticle(article: Article) = withContext(Dispatchers.IO) {
        db.saveArticle(article.asEntity())
    }

    suspend fun unSaveArticle(article: Article) = withContext(Dispatchers.IO) {
        db.deleteArticle(article.asEntity())
    }

    suspend fun getSavedArticles() = withContext(Dispatchers.IO) {
        db.getSavedArticles().map { it.asExternalModel() }
    }

    suspend fun isArticleSaved(articleId: String): Boolean = withContext(Dispatchers.IO) {
        db.isArticleSaved(articleId)
    }

}