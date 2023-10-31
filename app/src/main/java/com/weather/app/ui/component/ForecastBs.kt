package com.weather.app.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastBs(
    modifier: Modifier = Modifier,
    items: List<ForecastItemData>,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Spacer(modifier = modifier.height(16.dp))
        if (items.isNotEmpty()) {
            LazyColumn {
                items(items) { item ->
                    ForecastItem(
                        data = ForecastItemData(
                            date = item.date,
                            humidity = item.humidity,
                            wind = item.wind,
                            temp = item.temp
                        )
                    )
                }
            }
        } else {
            Text(text = "Empty Data")
        }
        Spacer(modifier = modifier.height(50.dp))
    }
}