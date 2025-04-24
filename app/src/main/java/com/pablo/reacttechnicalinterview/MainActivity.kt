package com.pablo.reacttechnicalinterview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.pablo.reacttechnicalinterview.core.navigation.Navigation
import com.pablo.reacttechnicalinterview.ui.theme.ReactTechnicalInterviewTheme
import com.pablo.reacttechnicalinterview.ui.viewmodels.MainActivityViewModel
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.exception.InitException
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReactTechnicalInterviewTheme {

                val viewModel: MainActivityViewModel = hiltViewModel()

                Navigation(viewModel)
                FaceSDK.Instance()
                    .initialize(applicationContext) { status: Boolean, exception: InitException? ->
                        run {
                            Log.d("MainActivity", "Status: $status")

                        }
                    }
            }
        }
    }
}
