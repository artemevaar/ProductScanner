package com.example.productscanner
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.compose.rememberImagePainter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ScanScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun imageDisplayed_whenCapturedImageUriIsNotNull() {
        val uri = Uri.parse("barcode.jpg")
        composeTestRule.setContent {
            var capturedImageUri by remember { mutableStateOf(uri) }
            CompositionLocalProvider(LocalContext provides ApplicationProvider
                .getApplicationContext()) {
                Column { if (capturedImageUri != null) {
                        Image(painter = rememberImagePainter(capturedImageUri),
                            contentDescription = null,
                            modifier = Modifier.size(344.dp, 180.dp)
                                .testTag("SelectedImage"))
                    }
                }
            }
        }
        composeTestRule.onNodeWithTag("SelectedImage").assertIsDisplayed()
    }

    @Test
    fun cameraPreviewDisplayed_whenCapturedImageUriIsNull() {
        composeTestRule.setContent {
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
            CompositionLocalProvider(LocalContext provides ApplicationProvider
                .getApplicationContext()) {
                Column {
                    if (capturedImageUri == null) {
                        Box(Modifier.testTag("CameraPreview")) {
                            Text("Camera preview here")
                        }
                    }
                }
            }
        }
        composeTestRule.onNodeWithTag("CameraPreview").assertIsDisplayed()
    }

}
