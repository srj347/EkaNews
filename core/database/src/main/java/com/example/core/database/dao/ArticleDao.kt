package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertArticle(article: ArticleEntity): Long

    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<ArticleEntity>
}