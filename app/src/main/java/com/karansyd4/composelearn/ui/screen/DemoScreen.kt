package com.karansyd4.composelearn.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DemoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f)
                .background(color = Color.Green)
        ) {
            Text(text = "60")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.4f)
                .background(color = Color.Blue)
        ) {
            Text(text = "40")
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewDemoScreen() {
    DemoScreen()
}