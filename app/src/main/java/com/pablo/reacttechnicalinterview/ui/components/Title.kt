package com.pablo.reacttechnicalinterview.ui.components





import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        color = Color.Black,
        modifier = modifier
            .padding(16.dp),
            // .align(Alignment.CenterHorizontally),
        style = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    )

}