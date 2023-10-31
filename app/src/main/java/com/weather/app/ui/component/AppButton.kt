package com.weather.app.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.app.ui.theme.ButtonEnableColor

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    isActive: Boolean = false,
    onButtonClick: () -> Unit = {}
) {
    val containerColor = if (isActive) Color.Transparent else ButtonEnableColor
    val contentColor = if (isActive) Color.Black else Color.White
    val border = if (isActive) BorderStroke(1.dp, Color.Black) else null

    Button(
        modifier = modifier.defaultMinSize(minWidth = 150.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        onClick = onButtonClick
    ) {
        Text(text = buttonText)
    }
}

@Preview(showBackground = true)
@Composable
fun AppButtonPrev() {
    AppButton(buttonText = "Save")
}