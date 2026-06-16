package com.example.jango

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.jango.loginscreen.LoginScreen
import com.example.jango.ui.theme.JangoTheme
import homescreen.DashboardScreen
import homescreen.RoomServiceScreen
import homescreen.BookingScreen
import homescreen.Room
import profile.ProfileScreen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase Database reference using the standard method
        database = FirebaseDatabase.getInstance("https://j575424-default-rtdb.firebaseio.com").reference

        enableEdgeToEdge()
        setContent {
            JangoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var currentScreen by remember { mutableStateOf("login") }
                    var userEmail by remember { mutableStateOf("") }

                    when (currentScreen) {
                        "login" -> {
                            LoginScreen { email ->
                                userEmail = email
                                sendWelcomeEmail(email)
                                currentScreen = "dashboard"
                            }
                        }
                        "dashboard" -> {
                            DashboardScreen(
                                onRoomServiceClick = {
                                    currentScreen = "room_service"
                                },
                                onProfileClick = {
                                    currentScreen = "profile"
                                },
                                onBookRoomClick = {
                                    currentScreen = "booking"
                                }
                            )
                        }
                        "room_service" -> {
                            RoomServiceScreen(onBackClick = {
                                currentScreen = "dashboard"
                            })
                        }
                        "profile" -> {
                            ProfileScreen(onBackClick = {
                                currentScreen = "dashboard"
                            })
                        }
                        "booking" -> {
                            BookingScreen(
                                onBackClick = {
                                    currentScreen = "dashboard"
                                },
                                onBookNow = { room ->
                                    sendBookingEmail(room, userEmail)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendWelcomeEmail(email: String) {
        val subject = "Welcome to LUXURY STAY!"
        val body = """
            Hello,
            
            Welcome to LUXURY STAY! We are thrilled to have you with us.
            
            You can now use our app to book rooms and order room service directly from your phone.
            
            Enjoy your stay!
            
            Regards,
            LUXURY STAY Management
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun sendBookingEmail(room: Room, email: String) {
        val subject = "Booking Confirmation: ${room.name}"
        val body = """
            Hello,
            
            Thank you for booking with LUXURY STAY!
            
            Your booking details:
            Room: ${room.name}
            Category: ${room.category}
            Price: ${room.price}
            Description: ${room.description}
            
            We look forward to seeing you.
            
            Regards,
            LUXURY STAY Management
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Fallback if no email app is found
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(Intent.createChooser(shareIntent, "Send Email"))
        }
    }
}
