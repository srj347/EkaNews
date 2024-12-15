package com.example.core.database.converters

import androidx.room.TypeConverter
import com.example.core.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun fromSourceString(name: String): Source {
        return Source(name,name)
    }
}