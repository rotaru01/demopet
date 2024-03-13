package com.danielrotaru.petdemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielrotaru.petdemo.ui.theme.PetDemoTheme
import com.danielrotaru.petdemo.views.AnimalsOverviewScreen
import com.danielrotaru.petdemo.views.DetailsScreen
import com.danielrotaru.petdemo.views.viewmodel.PetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetDemoTheme {

                val navController = rememberNavController()
                val viewModel = viewModel<PetViewModel>()
                val currentDestination by navController.currentBackStackEntryAsState()

                val showNavigationIcon =
                    currentDestination?.destination?.route != AnimalsDestination.Overview.route
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Animals",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                },
                                navigationIcon = {
                                    if (showNavigationIcon) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                            )
                        },

                        content = {
                            val x = it
                            NavHost(
                                navController = navController,
                                startDestination = AnimalsDestination.Overview.route
                            ) {

                                composable(route = AnimalsDestination.Overview.route) {
                                    AnimalsOverviewScreen(
                                        viewModel = viewModel
                                    ) {
                                        navController.navigate("Details" + "/${it.id}") {}
                                    }
                                }
                                composable(route = AnimalsDestination.Details.route,
                                    arguments = listOf(navArgument("animal"){
                                    type = NavType.IntType
                                })) {navBackStack->

                                    DetailsScreen(
                                        navController = navController,
                                        viewModel = viewModel,
                                        animalId = navBackStack.arguments?.getInt("animal")?:1
                                    )
                                }

                            }
                        })
                }
            }
        }
    }
}

sealed class AnimalsDestination(val route: String) {
    object Overview : AnimalsDestination("Overview")
    object Details : AnimalsDestination("Details/{animal}")
}