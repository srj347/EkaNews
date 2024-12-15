package com.example.ekanews

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.headline.screens.HeadLineScreen
import com.example.core.model.Article
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.core.ui.navigation.Screen
import com.example.core.ui.utils.Utils
import com.example.ekanews.navigation.BottomNavigationBar
import com.example.ekanews.screens.HeadLineDetailsScreen
import com.example.ekanews.screens.SearchScreen
import com.example.feature.saved.SavedScreen
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: NewsViewModel, context: Activity, isConnected: Boolean) {
    val TAG = "MainScreen"
    val navController = rememberNavController()
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val bottomNavRoutes = setOf(
        Screen.HeadLine.route,
        Screen.Search.route,
        Screen.Save.route
    )

//    LaunchedEffect(isConnected) {
//        if (!isConnected) {
//            snackbarHostState.showSnackbar(Localization.NO_INTERNET_CONNECTION)
//        }
//    }

    Scaffold(
        bottomBar = {
            if (currentBackStackEntry.value?.destination?.route in bottomNavRoutes) {
                BottomNavigationBar(navController = navController)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HeadLine.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(
                Screen.HeadLine.route
            ) {
                HeadLineScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onClick = { article -> },
                    isConnected
                )
            }
            slideUpComposable(
                route = Screen.Search.route,
                content = {
                    SearchScreen(
                        viewModel,
                        navController = navController,
                        isConnected
                    )
                }
            )
            composable(Screen.Save.route) {
                SavedScreen(
                    viewModel,
                    onClick = { article ->
                        val json = URLEncoder.encode(Json.encodeToString(article), "UTF-8")
                        navController.navigate(
                            "headLineDetail/${
                                json
                            }"
                        )
                    },
                    onShareClick = { url -> Utils.shareContent(context, url) }
                )
            }
            composable(
                Screen.HeadLineDetail.route,
                arguments = listOf(navArgument("article") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemJson = backStackEntry.arguments?.getString("article")
                val item =
                    Gson().fromJson(URLDecoder.decode(itemJson, "UTF-8"), Article::class.java)
                HeadLineDetailsScreen(
                    context, onBackClick = { navController.navigateUp() },
                    item
                )
            }
        }
    }
}


fun NavGraphBuilder.slideUpComposable(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(500)
            )
        }
    ) {
        content()
    }
}


