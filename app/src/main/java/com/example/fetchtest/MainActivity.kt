package com.example.fetchtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fetchtest.ui.theme.FetchTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = FetchApiService.create()

        setContent {
            var items by remember { mutableStateOf<List<FetchList>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val rawItems = api.getItems()
                    items = processItems(rawItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val groupedItems = items.groupBy { it.listId }

            FetchTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(12.dp)
                    ) {
                        groupedItems.forEach { (listId, itemList) ->
                            item {
                                Text(
                                    text = "List $listId",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(itemList) { item ->
                                Text(
                                    text = item.name ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
