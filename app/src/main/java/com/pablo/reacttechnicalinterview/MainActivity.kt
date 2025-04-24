package com.pablo.reacttechnicalinterview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pablo.reacttechnicalinterview.core.navigation.Navigation
import com.pablo.reacttechnicalinterview.ui.theme.ReactTechnicalInterviewTheme
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

                Navigation()
                FaceSDK.Instance()
                    .initialize(applicationContext) { status: Boolean, exception: InitException? ->
                        run {
                            Log.i("TAG", "Status: $status")


                        }
                    }
            }
        }
    }
}
