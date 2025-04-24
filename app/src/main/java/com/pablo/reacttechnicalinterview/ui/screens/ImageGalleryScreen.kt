package com.pablo.reacttechnicalinterview.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pablo.reacttechnicalinterview.ui.viewmodels.MainActivityViewModel
import com.pablo.reacttechnicalinterview.ui.components.NextScreenButton
import com.pablo.reacttechnicalinterview.R

@Composable
fun ImageGalleryScreen(
    viewModel: MainActivityViewModel,
    navigateToNextScreen: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val isImageSelected = rememberSaveable { mutableStateOf(true) }

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
            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri ->
                    uri?.let {
                        viewModel.updateImageFromGallery(uri)
                        isImageSelected.value = true
                    }
                }
            )
            Text(
                text = stringResource(R.string.title_image_gallery_screen),
                color = Color.Black,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            // Title(text = stringResource(R.string.title_image_gallery_screen))
            Spacer(Modifier.height(16.dp))
            AsyncImage(
                model = state.imageFromGallery,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(250.dp)
                    .width(250.dp),
            )
            Spacer(Modifier.height(16.dp))


            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(10.dp)
            ) {
                Text(text = stringResource(R.string.pick_image_from_gallery))
            }
            Spacer(Modifier.height(32.dp))

            NextScreenButton(
                text = "Next",
                onClick = navigateToNextScreen
            )

        }
    }
}

