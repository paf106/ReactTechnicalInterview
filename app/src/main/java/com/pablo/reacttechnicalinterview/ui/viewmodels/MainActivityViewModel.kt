package com.pablo.reacttechnicalinterview.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.pablo.reacttechnicalinterview.di.LocalModule
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class MainActivityState(

    // Variables for passing screens


    val imageFromGallery: Uri? = null,
    val imageFromSDK: Bitmap? = null,
    val similarityText: String = "",
)

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityState())
    val uiState: StateFlow<MainActivityState> = _uiState.asStateFlow()


    fun startSDK(context: Context) {
        FaceSDK.Instance()
            .initialize(context) { status: Boolean, exception: Exception? ->
                run {
                    if (status) {
                        // SDK initialized successfully
                    } else {
                        // Handle initialization error
                    }
                }
            }
    }

    fun stopSDK() {
        FaceSDK.Instance().deinitialize()
    }

    fun startFaceCapture(context: Context) {

        val configuration = FaceCaptureConfiguration.Builder()
            .setCameraId(1)
            .setCameraSwitchEnabled(true)
            .build()

        FaceSDK.Instance().presentFaceCaptureActivity(
            context,
            configuration
        ) { response ->

            response.image?.let { image -> updateImageFromSDK(image.bitmap) }

        }
    }

    fun updateImageFromGallery(uri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                imageFromGallery = uri
            )
        }
    }

    fun updateImageFromSDK(bitmap: Bitmap) {
        _uiState.update { currentState ->
            currentState.copy(
                imageFromSDK = bitmap
            )
        }
    }

    fun updateSimilarityText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                similarityText = text
            )
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null

        }
    }

    fun resetState() {
        _uiState.update { currentState ->
            currentState.copy(
                imageFromGallery = null,
                imageFromSDK = null,
                similarityText = ""
            )
        }
    }

}