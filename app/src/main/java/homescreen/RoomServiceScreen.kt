package homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class RoomServiceOption(val name: String, val description: String, val price: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomServiceScreen(onBackClick: () -> Unit) {
    val options = listOf(
        RoomServiceOption("Continental Breakfast", "Fresh pastries, fruit, coffee, and juice.", "KSh 1,500"),
        RoomServiceOption("Club Sandwich", "Classic triple-decker with fries.", "KSh 1,250"),
        RoomServiceOption("Spaghetti Carbonara", "Traditional Italian pasta with pancetta.", "KSh 1,800"),
        RoomServiceOption("Fresh Fruit Platter", "Seasonal sliced fruits.", "KSh 1,000"),
        RoomServiceOption("Mineral Water (Large)", "Still or Sparkling.", "KSh 400"),
        RoomServiceOption("Extra Pillow/Blanket", "Request additional bedding.", "Free"),
        RoomServiceOption("Ironing Service", "Per item pressing.", "KSh 500")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Room Service", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Available Services",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(options) { option ->
                ServiceItemCard(option)
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun ServiceItemCard(option: RoomServiceOption) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = { /* TODO: Select option */ }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(option.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(option.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(option.price, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomServicePreview() {
    MaterialTheme {
        RoomServiceScreen(onBackClick = {})
    }
}
