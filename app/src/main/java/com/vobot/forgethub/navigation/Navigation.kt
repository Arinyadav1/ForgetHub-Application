package com.vobot.forgethub.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vobot.forgethub.model.DataOfUi
import com.vobot.forgethub.screen.MainScreen
import com.vobot.forgethub.screen.NoteScreen
import com.vobot.forgethub.viewModel.DataViewModel
import com.vobot.forgethub.viewModel.DataViewModelFactory
import com.vobot.forgethub.roomDatabase.ForgetHubDataDatabase
import com.vobot.forgethub.reprository.DataRepo


@SuppressLint("UnrememberedMutableState")
@Composable
fun App() {
    val context = LocalContext.current
    val database = ForgetHubDataDatabase.getDatabase(context)
    val repository = DataRepo(database.thoughtDataDAO(), dataOfUi = DataOfUi())
    val dataViewModel: DataViewModel =
        viewModel(factory = DataViewModelFactory(repository, context))
    val navController = rememberNavController()
    val interactionSource = remember { MutableInteractionSource() }


    NavHost(
        navController = navController,
        startDestination = "homeScreen",
    ) {
        composable(route = "homeScreen",

            exitTransition = {
                when (targetState.destination.route) {
                    "firstTimeDataSaveScreen",
                    "preSaveDataEditScreen/{id}/{title}/{description}",
                    -> fadeOut(animationSpec = tween(100))

                    else -> null
                }

            }) {
            MainScreen(navController, dataViewModel, context, interactionSource)
        }

        composable(
            route = "preSaveDataEditScreen/{id}/{title}/{description}",
            enterTransition = {
                fadeIn(animationSpec = tween(200, easing = LinearEasing)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(200, easing = EaseIn)
                )

            },
            exitTransition = {
                fadeOut(animationSpec = tween(200, easing = LinearEasing)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(200, easing = EaseIn)
                )

            },
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
            ),


            ) { it ->
            val id = it.arguments?.getInt("id")
            val title = it.arguments?.getString("title")
            val description = it.arguments?.getString("description")
            NoteScreen(
                navController, dataViewModel, id, title, description, context, interactionSource
            )
        }

        composable(
            route = "firstTimeDataSaveScreen",

            enterTransition = {
                fadeIn(animationSpec = tween(200, easing = LinearEasing)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(200, easing = EaseIn)
                )

            },
            exitTransition = {
                fadeOut(animationSpec = tween(200, easing = LinearEasing)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(200, easing = EaseIn)
                )

            },
        ) {

            NoteScreen(
                navController, dataViewModel, null, "", "", context, interactionSource
            )


        }

    }

}

