package com.example.ekanews

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.repository.NewsLocalRepository
import com.example.core.data.repository.NewsRemoteRepository
import com.example.core.database.ArticleDatabase
import com.example.core.network.util.RetrofitInstance
import com.example.core.ui.viewmodel.NetworkViewModel
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.core.ui.viewmodel.NewsViewModelProviderFactory

class MainActivity : ComponentActivity() {
    private lateinit var networkViewModel: NetworkViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        networkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
        networkViewModel.registerReceiver(this)

        // TODO: Introduce DI to inject them
        val articleDao = ArticleDatabase.invoke(this).getArticleDao()
        val savedDao = ArticleDatabase.invoke(this).getSavedArticleDao()
        val api = RetrofitInstance.api
        val newsViewModelFactory = NewsViewModelProviderFactory(NewsRemoteRepository(api, articleDao), NewsLocalRepository(savedDao))
        val viewmodel: NewsViewModel = ViewModelProvider(this, newsViewModelFactory)[NewsViewModel::class.java]

        viewmodel.getBreakingNews()

        setContent {
            val isConnected by networkViewModel.isConnected.observeAsState(initial = true)
            MainScreen(viewmodel,this, isConnected)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkViewModel.unregisterReceiver(this)
    }
}
