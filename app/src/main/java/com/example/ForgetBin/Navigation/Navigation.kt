package com.example.ForgetBin.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ForgetBin.Screen.MainScreen
import com.example.ForgetBin.Screen.NoteScreen
import com.example.ForgetBin.ViewModel.DataViewModel
import com.example.ForgetBin.ViewModel.DataViewModelFactory
import com.example.ForgetBin.database.ThoughtsDataDatabase
import com.example.ForgetBin.reprository.DataRepo


@Composable
fun App() {

    val context = LocalContext.current
    val database = ThoughtsDataDatabase.getDatabase(context)
    val repository = DataRepo(database.thoughtDataDAO())
    val dataViewModel: DataViewModel = viewModel(factory = DataViewModelFactory(repository))

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable(route = "mainScreen") {
            MainScreen(navController, dataViewModel, null)
        }

        composable(
            route = "screen/{id}/{title}/{description}/{date}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType })
        ) { it ->
            val id = it.arguments?.getInt("id")
            val title = it.arguments?.getString("title")
            val description = it.arguments?.getString("description")
            val date = it.arguments?.getString("date")
            NoteScreen(navController, dataViewModel, id, title, description, date)
        }

        composable(route = "NoteScreen") {
            NoteScreen(navController, dataViewModel, null, null, null, null)
        }

        composable(
            route = "deleteIcon/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { it ->
            val id = it.arguments?.getInt("id")

            MainScreen(navController, dataViewModel, id)

        }

    }
}