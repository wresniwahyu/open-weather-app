package com.weather.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ForecastGroupItem(
    modifier: Modifier = Modifier,
    data: ForecastGroupItemData,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.date,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "L: ${data.minTemp}˚C")
            Spacer(modifier = Modifier.width(8.dp))
            LinearIndicatorWithGradientColor(
                modifier = Modifier.weight(1f),
                progress = 100f
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "H: ${data.maxTemp}˚C")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }
}

data class ForecastGroupItemData(
    val date: String,
    val minTemp: Double,
    val maxTemp: Double
)

@Preview
@Composable
fun ForecastGroupItemPrev() {
    ForecastGroupItem(
        data = ForecastGroupItemData(
            date = "Monday, 24-Jan-2023",
            minTemp = 20.0,
            maxTemp = 30.0
        )
    )
}