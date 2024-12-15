package com.example.core.network.model

import com.example.core.database.entity.ArticleEntity
import com.example.core.database.entity.generateArticleUniqueId
import com.example.core.model.Article
import com.example.core.model.Source
import java.io.Serializable

data class ArticleResponse(
    val id: String? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable

fun ArticleResponse.asEntity(): ArticleEntity =
    ArticleEntity(
        id = generateArticleUniqueId(title, url, publishedAt),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = Source(source?.id, source?.name),
        title = title,
        url = url,
        urlToImage = urlToImage
    )

fun ArticleResponse.asExternalModel(): Article =
    Article(
        id = generateArticleUniqueId(title, url, publishedAt),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source?.name,
        title = title,
        url = url,
        urlToImage = urlToImage
    )