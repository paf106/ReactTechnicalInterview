package com.pablo.reacttechnicalinterview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit
import com.regula.facesdk.request.MatchFacesRequest

@Composable
fun ComparisonScreen(
    viewModel: MainActivityViewModel,
    navigateToNextScreen: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Compare Images",
                color = Color.Black,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = state.imageFromSDK,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(100.dp)
                        .width(100.dp),
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    modifier = Modifier.size(50.dp),
                    contentDescription = null

                )
                Spacer(Modifier.width(16.dp))
                AsyncImage(
                    model = state.imageFromGallery,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(100.dp)
                        .width(100.dp),
                )
            }

            Spacer(Modifier.height(32.dp))


            Button(
                onClick = {
                    val request = MatchFacesRequest(
                        listOf(
                            MatchFacesImage(state.imageFromGallery?.let {
                                viewModel.getBitmapFromUri(
                                    context,
                                    it
                                )
                            }, ImageType.PRINTED),
                            MatchFacesImage(state.imageFromSDK, ImageType.LIVE),
                        )
                    )
                    FaceSDK.Instance()
                        .matchFaces(context, request) { response ->

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

                            viewModel.updateSimilarityText(similarity?.let {
                                "Similarity: " + String.format(
                                    "%.2f",
                                    it * 100
                                ) + "%"
                            } ?: response.exception?.let {
                                "Similarity: " + it.message
                            } ?: "Similarity: ")

                        }

                }
            ) {
                Text("Compare faces!")
            }

            // Similarity text
            Text(
                text = state.similarityText,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(Modifier.height(32.dp))
            NextScreenButton(
                text = "Try new comparison",
                onClick = {
                    viewModel.resetState()
                    navigateToNextScreen()
                }
            )
        }
    }
}




