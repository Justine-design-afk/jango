package homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Room(val name: String, val category: String, val price: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(onBackClick: () -> Unit, onBookNow: (Room) -> Unit) {
    val rooms = listOf(
        // Premium
        Room("Royal Suite", "Premium", "KSh 25,000", "Ultimate luxury with city view and private jacuzzi."),
        Room("Presidential Suite", "Premium", "KSh 35,000", "Top floor, exclusive access, and personal butler."),
        
        // Executive
        Room("Executive Studio", "Executive", "KSh 15,000", "Spacious room with office desk and high-speed WiFi."),
        Room("Business Suite", "Executive", "KSh 18,000", "Perfect for business travelers with meeting space."),
        
        // Basic
        Room("Standard Single", "Basic", "KSh 5,000", "Cozy room with all essential amenities."),
        Room("Double Comfort", "Basic", "KSh 8,000", "Comfortable double bed for a relaxing stay.")
    )

    val categorizedRooms = rooms.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book a Room", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf("Premium", "Executive", "Basic").forEach { category ->
                item {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                val roomsInCategory = categorizedRooms[category] ?: emptyList()
                items(roomsInCategory) { room ->
                    RoomCard(room, onBookNow)
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun RoomCard(room: Room, onBookNow: (Room) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(room.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = room.price,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(room.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onBookNow(room) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Book Now")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    MaterialTheme {
        BookingScreen(onBackClick = {}, onBookNow = {})
    }
}
