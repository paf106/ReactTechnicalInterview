package com.pablo.reacttechnicalinterview.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NextScreenButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(end = 8.dp),
                color = Color.Black
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Next",
                tint = Color.Black
            )
        }
    }
}