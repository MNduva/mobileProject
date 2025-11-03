package com.example.semesterproject.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Remove duplicate MainActivity here (there's one in the root package).
// Keep this file focused on composables only.

@Composable
fun LandingScreen(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Mountain background at the bottom
        GeometricMountains(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.BottomCenter)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo placeholder (replace with your actual logo)
            LogoSection()

            Spacer(modifier = Modifier.height(48.dp))

            // Main heading
            Text(
                text = "Rent Agricultural Machinery Online",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D5F3F),
                lineHeight = 38.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fast, Easy & Reliable",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D5F3F),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Description
            Text(
                text = "Find the right tractor, harvester, or plow in your area. Book verified machines with or without an operator — get farming faster!",
                fontSize = 16.sp,
                color = Color(0xFF4A4A4A),
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button - wired to parameter
            Button(
                onClick = onSignUpClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7FB89A)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(120.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sign In Button - added for convenience (keeps your layout; original had no separate sign in here)
            TextButton(onClick = onSignInClick) {
                Text("Already have an account? Sign in", color = Color(0xFF2D5F3F))
            }
        }
    }
}

// Keep LogoSection here so other files in same package can reference it
@Composable
fun LogoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder for logo - replace with actual logo image
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌾",
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "GREENHIRE",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D5F3F),
            letterSpacing = 2.sp
        )

        Text(
            text = "RENT. HIRE. FARM.",
            fontSize = 10.sp,
            color = Color(0xFF6FA687),
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun GeometricMountains(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Mountain colors
        val darkGreen = Color(0xFF2D5F3F)
        val mediumGreen = Color(0xFF6FA687)
        val lightGreen = Color(0xFF9BC4A8)
        val slate = Color(0xFF5F7C8A)
        val mauve = Color(0xFF9B8C9B)

        // Back left mountain (light green)
        val backLeftPath = Path().apply {
            moveTo(0f, height)
            lineTo(0f, height * 0.3f)
            lineTo(width * 0.25f, height * 0.1f)
            lineTo(width * 0.4f, height * 0.5f)
            lineTo(width * 0.4f, height)
            close()
        }
        drawPath(backLeftPath, lightGreen)

        // Middle left mountain (medium green)
        val middleLeftPath = Path().apply {
            moveTo(0f, height)
            lineTo(width * 0.15f, height * 0.4f)
            lineTo(width * 0.35f, height * 0.15f)
            lineTo(width * 0.5f, height * 0.6f)
            lineTo(width * 0.5f, height)
            close()
        }
        drawPath(middleLeftPath, mediumGreen)

        // Front left mountain (dark green)
        val frontLeftPath = Path().apply {
            moveTo(0f, height)
            lineTo(width * 0.08f, height * 0.6f)
            lineTo(width * 0.28f, height * 0.3f)
            lineTo(width * 0.42f, height * 0.75f)
            lineTo(width * 0.42f, height)
            close()
        }
        drawPath(frontLeftPath, darkGreen)

        // Center tall mountain (dark green)
        val centerPath = Path().apply {
            moveTo(width * 0.35f, height)
            lineTo(width * 0.5f, height * 0.35f)
            lineTo(width * 0.58f, height * 0.05f)
            lineTo(width * 0.72f, height * 0.45f)
            lineTo(width * 0.72f, height)
            close()
        }
        drawPath(centerPath, darkGreen)

        // Right slate mountain
        val slatePath = Path().apply {
            moveTo(width * 0.55f, height)
            lineTo(width * 0.65f, height * 0.5f)
            lineTo(width * 0.78f, height * 0.35f)
            lineTo(width * 0.88f, height * 0.65f)
            lineTo(width * 0.88f, height)
            close()
        }
        drawPath(slatePath, slate)

        // Flar right mauve mountain
        val mauvePath = Path().apply {
            moveTo(width * 0.75f, height)
            lineTo(width * 0.82f, height * 0.6f)
            lineTo(width * 0.92f, height * 0.45f)
            lineTo(width, height * 0.7f)
            lineTo(width, height)
            close()
        }
        drawPath(mauvePath, mauve)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingScreenPreview() {
    LandingScreen()
}
