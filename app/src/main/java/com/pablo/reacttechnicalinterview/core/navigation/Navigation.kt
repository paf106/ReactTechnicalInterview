package com.pablo.reacttechnicalinterview.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pablo.reacttechnicalinterview.ui.screens.ComparisonScreen
import com.pablo.reacttechnicalinterview.ui.screens.ImageGalleryScreen
import com.pablo.reacttechnicalinterview.ui.screens.ImageSDKScreen
import com.pablo.reacttechnicalinterview.ui.viewmodels.MainActivityViewModel

@Composable
fun Navigation(viewModel: MainActivityViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ImageSDK) {
        composable<ImageSDK> {
            ImageSDKScreen(
                viewModel = viewModel,
                navigateToNextScreen = { navController.navigate(ImageGallery) }
            )

        }
        composable<ImageGallery> {
            ImageGalleryScreen(
                viewModel = viewModel,
                navigateToNextScreen = { navController.navigate(Comparison) }
            )

        }
        composable<Comparison> {
            ComparisonScreen(
                viewModel = viewModel,
                navigateToNextScreen = {
                    navController.navigate(ImageSDK) {
                        popUpTo<ImageSDK> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}