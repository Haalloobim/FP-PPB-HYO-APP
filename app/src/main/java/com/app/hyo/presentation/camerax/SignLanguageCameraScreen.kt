package com.app.hyo.presentation.camerax

import GestureOverlay
import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignLanguageCameraScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    var recognitionResult by remember { mutableStateOf<GestureRecognizerResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val gestureRecognizerHelper = remember {
        GestureRecognizerHelper(
            context = context,
            gestureRecognizerListener = object : GestureRecognizerHelper.GestureRecognizerListener {
                override fun onError(error: String) {
                    errorMessage = error
                }

                override fun onResults(result: GestureRecognizerResult) {
                    recognitionResult = result
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Preview use case
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        // Image analysis use case
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                                    gestureRecognizerHelper.recognizeLiveStream(imageProxy)
                                    imageProxy.close() // Make sure to close the proxy
                                }
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis // Add image analysis
                            )
                        } catch (exc: Exception) {
                            Log.e("CameraScreen", "Use case binding failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Overlay for drawing landmarks
            GestureOverlay(result = recognitionResult, modifier = Modifier.fillMaxSize())

            // Display results at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                val topGesture = recognitionResult?.gestures()?.firstOrNull()?.firstOrNull()
                Text(
                    text = "Detected: ${topGesture?.categoryName() ?: "None"}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Confidence: ${topGesture?.score()?.let { "%.2f".format(it) } ?: "N/A"}",
                    color = Color.White,
                    fontSize = 18.sp
                )
                if(errorMessage != null) {
                    Text(text = "Error: $errorMessage", color = Color.Red)
                }
            }

        } else {
            Text("Camera permission is required.", modifier = Modifier.align(Alignment.Center))
        }
    }
}