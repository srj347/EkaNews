package com.example.feature.saved

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.core.model.Article
import com.example.core.ui.icons.Bookmarks
import com.example.core.ui.utils.Utils


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedNewsCard(
    article: Article?,
    onShareClick: (String) -> Unit = {},
    onSaveClick: (Article) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            // Full-width image at the top
            AsyncImage(
                model = article?.urlToImage
                    ?: "https://upload.wikimedia.org/wikipedia/commons/1/1d/Bharat_Times_News_logo.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
            )
            Column(modifier = Modifier.padding(16.dp)) {
                // Title
                Text(
                    text = article?.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Description
                Text(
                    text = article?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Date and Icons Row
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Date
                    Text(
                        text = article?.publishedAt?.let { Utils.formatDate(it) } ?: "Unknown Date",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    // Share and Save Icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { article?.url?.let { onShareClick.invoke(it) } }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { article?.let { onSaveClick.invoke(it) } }) {
                            Icon(
                                imageVector = Icons.Rounded.Bookmarks,
                                contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}
