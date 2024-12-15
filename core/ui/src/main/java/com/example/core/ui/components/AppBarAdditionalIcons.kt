package com.example.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.ui.icons.Bookmarks

@Composable
fun AppBarAdditionalIcons(onShareIconClicked: () -> Unit, onSaveIconClicked : () -> Unit, isSaved: Boolean) {
    IconButton(
        onClick = {
            onSaveIconClicked()
        },
        modifier = Modifier.padding(2.dp)
    ) {
        Icon(
            imageVector = if(isSaved) Icons.Rounded.Bookmarks else Icons.Outlined.Bookmarks,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(25.dp)
        )
    }
    IconButton(
        onClick = {
            onShareIconClicked()
        },
        modifier = Modifier.padding(2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(25.dp)
        )
    }
}
