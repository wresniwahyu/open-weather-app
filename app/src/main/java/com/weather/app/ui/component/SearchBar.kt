package com.weather.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.app.R
import com.weather.app.ui.theme.IconColor

@Composable
fun SearchBar(
    initialValue: String = "",
    modifier: Modifier = Modifier,
    callback: (keyword: String) -> Unit
) {
    val inputValue = remember { mutableStateOf(initialValue) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .background(Color.White, CircleShape)
            .border(width = 1.dp, color = IconColor, shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            contentDescription = stringResource(id = R.string.search),
            imageVector = Icons.Default.Search,
            modifier = Modifier.padding(start = 16.dp),
            tint = IconColor
        )
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
                callback.invoke(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, CircleShape),
            placeholder = {
                Text(text = stringResource(id = R.string.search))
            },
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    return SearchBar(callback = {})
}