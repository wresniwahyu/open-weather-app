package com.weather.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.weather.app.R
import com.weather.app.data.model.FavoriteWeatherUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesBs(
    modifier: Modifier = Modifier,
    items: List<FavoriteWeatherUiModel>,
    onItemClicked: (item: FavoriteWeatherUiModel) -> Unit,
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
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { onItemClicked.invoke(item) },
                        text = item.name
                    )
                }
            }
        } else {
            Text(text = stringResource(R.string.favorite_is_empty))
        }
        Spacer(modifier = modifier.height(50.dp))
    }
}