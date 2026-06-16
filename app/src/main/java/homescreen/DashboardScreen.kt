package homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuickAction(val title: String, val icon: ImageVector)
data class UserOrder(val dishName: String, val status: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onRoomServiceClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBookRoomClick: () -> Unit,
) {
    var feedbackText by remember { mutableStateOf("") }
    val orders = remember { mutableStateListOf<UserOrder>() }
    
    // Some initial data
    LaunchedEffect(Unit) {
        if (orders.isEmpty()) {
            orders.add(UserOrder("Gourmet Truffle Pasta", "Preparing", "12:30 PM"))
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Column {
                        Text("Welcome Back,", style = MaterialTheme.typography.titleMedium)
                        Text("Hotel Guest", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Quick Actions Section
            item {
                Text("Quick Services", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val actions = listOf(
                        QuickAction("Room Service", Icons.Default.RoomService),
                        QuickAction("Book Room", Icons.Default.Hotel),
                        QuickAction("Housekeeping", Icons.Default.CleaningServices),
                        QuickAction("Laundry", Icons.Default.LocalLaundryService),
                        QuickAction("Concierge", Icons.Default.SupportAgent)
                    )
                    items(actions) { action ->
                        ActionCard(action, onClick = {
                            when (action.title) {
                                "Room Service" -> onRoomServiceClick()
                                "Book Room" -> onBookRoomClick()
                            }
                        })
                    }
                }
            }

            // Order Status Section (Stored Data Display)
            item {
                Text("Active Orders", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                if (orders.isEmpty()) {
                    Text("No active orders", color = Color.Gray)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        orders.forEach { order ->
                            OrderCard(order)
                        }
                    }
                }
            }

            // User Input Section (Feedback/Request)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Special Request & Feedback", 
                            style = MaterialTheme.typography.titleMedium, 
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = feedbackText,
                            onValueChange = { feedbackText = it },
                            placeholder = { Text("How can we make your stay better?") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { 
                                if (feedbackText.isNotBlank()) {
                                    // Storing feedback locally or via log for now
                                    println("Feedback Stored: $feedbackText")
                                    feedbackText = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Send Request")
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(action: QuickAction, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.size(110.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(action.icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(action.title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun OrderCard(order: UserOrder) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(order.dishName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text("Ordered at ${order.time}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = order.status, 
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    MaterialTheme {
        DashboardScreen(onRoomServiceClick = {}, onProfileClick = {}, onBookRoomClick = {})
    }
}
