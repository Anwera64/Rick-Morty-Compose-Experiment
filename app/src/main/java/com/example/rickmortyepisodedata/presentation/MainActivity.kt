package com.example.rickmortyepisodedata.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickmortyepisodedata.presentation.details.EpisodeDetailsScreen
import com.example.rickmortyepisodedata.presentation.details.ID_ARGUMENT_KEY
import com.example.rickmortyepisodedata.presentation.episodes.EPISODES_KEY
import com.example.rickmortyepisodedata.presentation.episodes.EpisodesScreen
import com.example.rickmortyepisodedata.utils.LocalNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                NavHost(navController = navController, startDestination = EPISODES_KEY) {
                    mainNavGraph()
                }
            }
        }
    }

    private fun NavGraphBuilder.mainNavGraph() {
        composable(EPISODES_KEY) {
            EpisodesScreen()
        }
        composable(
            route = "$EPISODES_KEY/{id}",
            arguments = listOf(
                navArgument(ID_ARGUMENT_KEY) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(ID_ARGUMENT_KEY) ?: error("Missing ID")
            EpisodeDetailsScreen(id)
        }
    }
}