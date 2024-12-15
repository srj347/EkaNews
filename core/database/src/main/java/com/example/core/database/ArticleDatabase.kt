package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.database.converters.Converters
import com.example.core.database.dao.ArticleDao
import com.example.core.database.dao.SavedArticleDao
import com.example.core.database.entity.ArticleEntity
import com.example.core.database.entity.SavedArticleEntity

@Database(
    entities = [ArticleEntity::class, SavedArticleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase :RoomDatabase(){

    abstract fun getArticleDao(): ArticleDao
    abstract fun getSavedArticleDao(): SavedArticleDao

    companion object{
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}