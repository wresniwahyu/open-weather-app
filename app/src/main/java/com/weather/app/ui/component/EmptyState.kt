package com.weather.app.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.app.R

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    text: String,
    buttonText: String = "",
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (buttonText.isNotBlank()) {
            AppButton(buttonText = buttonText) {
                onButtonClick.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePrev() {
    EmptyState(
        text = stringResource(id = R.string.message_network_unavailable),
        buttonText = "Action"
    )
}