package com.example.core.data.repository.mapper

import com.example.core.database.entity.SavedArticleEntity
import com.example.core.database.entity.generateArticleUniqueId
import com.example.core.model.Article
import com.example.core.model.Source

fun Article.asEntity(): SavedArticleEntity =
    SavedArticleEntity(
        id = id ?: generateArticleUniqueId(title, url, publishedAt),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = Source(id = source.toString(), name = source.toString()),
        title = title,
        url = url,
        urlToImage = urlToImage
    )