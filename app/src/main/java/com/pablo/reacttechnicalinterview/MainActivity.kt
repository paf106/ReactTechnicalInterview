package com.pablo.reacttechnicalinterview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.pablo.reacttechnicalinterview.ui.theme.ReactTechnicalInterviewTheme
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.exception.InitException
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit
import com.regula.facesdk.request.MatchFacesRequest
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReactTechnicalInterviewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->

                    val context = LocalContext.current

                    var imageFromSDK by rememberSaveable { mutableStateOf<Bitmap?>(null) }
                    var similarityText by rememberSaveable { mutableStateOf("") }
                    var imageFromGallery by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }


                    val galleryLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri ->
                            uri?.let {
                                imageFromGallery = it
                            }
                        }
                    )

                    FaceSDK.Instance()
                        .initialize(context) { status: Boolean, exception: InitException? ->
                            run {
                                Log.i("TAG", "Status: $status")


                            }
                        }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(imageFromSDK),
                            contentDescription = null,
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                // galleryLauncher.launch("image/*")
                                val configuration = FaceCaptureConfiguration.Builder()
                                    .setCameraId(1)
                                    .setCameraSwitchEnabled(true)
                                    .build()

                                FaceSDK.Instance().presentFaceCaptureActivity(
                                    applicationContext,
                                    configuration
                                ) { response ->

                                    // ... check response.image for capture result.
                                    response.let {
                                        imageFromSDK = it.image?.bitmap
                                        Log.i("result", "Response: ${it.image}")
                                    }
                                    Log.i("result", "Response: ${response.image}")
                                }
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp)
                        ) {
                            Text(text = "Take photo using SDK")
                        }
                        Image(
                            painter = rememberAsyncImagePainter(imageFromGallery),
                            contentDescription = null,
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp)
                        ) {
                            Text(text = "Pick an image from gallery")
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                imageFromGallery.let {


                                    val bitmap = uriToBitmap(it)
                                    bitmap?.let { bitmap2 ->

                                        val request = MatchFacesRequest(
                                            listOf(
                                                MatchFacesImage(bitmap, ImageType.PRINTED),
                                                MatchFacesImage(bitmap2, ImageType.LIVE),
                                            )
                                        )
                                        FaceSDK.Instance()
                                            .matchFaces(applicationContext, request) { response ->
                                                val split = MatchFacesSimilarityThresholdSplit(
                                                    response.results,
                                                    0.75
                                                )
                                                val similarity = if (split.matchedFaces.size > 0) {
                                                    split.matchedFaces[0].similarity
                                                } else if (split.unmatchedFaces.size > 0) {
                                                    split.unmatchedFaces[0].similarity
                                                } else {
                                                    null
                                                }

                                                similarityText = similarity?.let {
                                                    "Similarity: " + String.format(
                                                        "%.2f",
                                                        it * 100
                                                    ) + "%"
                                                } ?: response.exception?.let {
                                                    "Similarity: " + it.message
                                                } ?: "Similarity: "

                                            }
                                    }
                                }
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp)
                        ) {
                            Text(text = "Make photo comparison")
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(similarityText)
                    }


                }
            }
        }
    }

    fun faceCapture() {

        val configuration = FaceCaptureConfiguration.Builder()
            .setCameraId(1)
            .setCameraSwitchEnabled(true)
            .build()

        FaceSDK.Instance().presentFaceCaptureActivity(
            applicationContext,
            configuration
        ) { response ->

            // ... check response.image for capture result.
            response.let {

            }
            Log.i("result", "Response: ${response.image}")
        }

    }

    fun matchFaces(
        image1: Bitmap,
        image2: Bitmap,
    ) {
        val request = MatchFacesRequest(
            listOf(
                MatchFacesImage(image1, ImageType.PRINTED),
                MatchFacesImage(image2, ImageType.PRINTED),
            )
        )
        FaceSDK.Instance().matchFaces(applicationContext, request) { result ->
            Log.i("result2", "Response: ${result.results.size}")

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

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor!!.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
