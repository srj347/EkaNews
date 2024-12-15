package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Article
import com.example.core.model.Source

@Entity(tableName = "Article")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val isSaved: Boolean = false
)

fun ArticleEntity.asExternalModel(): Article =
    Article(
        id = id,
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source?.name,
        title = title,
        url = url,
        urlToImage = urlToImage
    )

fun generateArticleUniqueId(title: String?, url: String?, publishedAt: String?): String {
    val combinedString = "${title.orEmpty()}-${url.orEmpty()}-${publishedAt.orEmpty()}"
    return combinedString.hashCode().toString()
}