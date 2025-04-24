package com.pablo.reacttechnicalinterview.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pablo.reacttechnicalinterview.ComparisonScreen
import com.pablo.reacttechnicalinterview.ImageGalleryScreen
import com.pablo.reacttechnicalinterview.ImageSDKScreen
import com.pablo.reacttechnicalinterview.MainActivityViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val viewModel = viewModel { MainActivityViewModel() }

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
                navigateToNextScreen = { navController.navigate(ImageSDK) {
                    popUpTo<ImageSDK> {
                        inclusive = true
                    }
                } }
            )

        }
    }

}