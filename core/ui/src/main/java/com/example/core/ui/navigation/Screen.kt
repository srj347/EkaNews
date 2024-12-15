package com.example.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.ui.icons.Bookmarks

sealed class Screen(val route: String, val title: String, val selectedIcon: ImageVector?, val unSelectedIcon: ImageVector?) {
    object HeadLine : Screen("headline", "Headline", Icons.Default.Home, Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search, Icons.Default.Search)
    object Save : Screen("save", "Saved", Icons.Rounded.Bookmarks, Icons.Outlined.Bookmarks)
    object HeadLineDetail : Screen("headLineDetail/{article}","Article",null, null){
        fun createRoute(article: String) = "headLineDetail/$article"
    }
}