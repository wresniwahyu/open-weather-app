package com.weather.app.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weather.app.R

@Composable
fun ForecastItem(
    modifier: Modifier = Modifier,
    data: ForecastItemData
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = data.date,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Humidity: ${data.humidity}%",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Wind: ${data.wind} m/s",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                modifier = Modifier
                    .defaultMinSize(50.dp, 50.dp),
                model = stringResource(R.string.format_icon_url, data.icon),
                contentScale = ContentScale.Inside,
                contentDescription = null,
            )
            Text(
                text = "${data.temp}˚C",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class ForecastItemData(
    val date: String,
    val humidity: Int,
    val wind: Double,
    val temp: Double,
    val icon: String
)

@Preview(showBackground = true)
@Composable
fun ForecastItemPrev() {
    ForecastItem(
        data = ForecastItemData(
            date = "Monday 24-Jan-2024 10:00",
            humidity = 41,
            wind = 5.81,
            temp = 32.0,
            icon = "10d"
        )
    )
}