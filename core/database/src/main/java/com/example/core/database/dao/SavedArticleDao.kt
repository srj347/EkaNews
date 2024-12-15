package com.example.core.database.dao

import androidx.room.*
import com.example.core.database.entity.ArticleEntity
import com.example.core.database.entity.SavedArticleEntity

@Dao
interface SavedArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveArticle(article: SavedArticleEntity)

    @Delete
    suspend fun deleteArticle(article: SavedArticleEntity)

    @Query("SELECT * FROM SavedArticle")
    suspend fun getSavedArticles(): List<SavedArticleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM SavedArticle WHERE id = :articleId)")
    suspend fun isArticleSaved(articleId: String): Boolean
}