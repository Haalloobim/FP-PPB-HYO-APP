package com.app.hyo.presentation.camerax

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

private lateinit var webSocket: WebSocket
private val client = OkHttpClient()

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalContext.current as LifecycleOwner

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            initWebSocket()
            startCamera(ctx, previewView, lifecycleOwner)

            previewView
        }
    )
}

private fun initWebSocket() {
    val request = Request.Builder()
        .url("ws://192.168.100.50:5000/ws")
        .build()

    webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "Connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received: $text")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Error", t)
        }
    })
}

private fun startCamera(context: Context, previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val rotation = previewView.display.rotation

        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .build()
            .also {
                it.setAnalyzer(Executors.newSingleThreadExecutor(), FrameAnalyzer())
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalyzer)
        } catch (e: Exception) {
            Log.e("CameraScreen", "Camera binding failed", e)
        }
    }, ContextCompat.getMainExecutor(context))
}

private class FrameAnalyzer : ImageAnalysis.Analyzer {
    private var lastSent = 0L
    private val intervalMs = 250L

    override fun analyze(image: ImageProxy) {
        val now = System.currentTimeMillis()
        if (now - lastSent < intervalMs) {
            image.close()
            return
        }

        val yuvImage = image.toYuvImage() ?: run {
            image.close()
            return
        }

        val stream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, image.width, image.height), 50, stream)
        val jpegBytes = stream.toByteArray()

        val bitmap = android.graphics.BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
        val rotatedBitmap = rotateBitmap(bitmap, image.imageInfo.rotationDegrees)
        val base64Image = bitmapToBase64(rotatedBitmap)
        sendImageViaWebSocket(base64Image)
        lastSent = now
        image.close()
    }

}

private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
    if (degrees == 0) return bitmap
    val matrix = android.graphics.Matrix().apply {
        postRotate(degrees.toFloat())
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun ImageProxy.toYuvImage(): android.graphics.YuvImage? {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    return android.graphics.YuvImage(nv21, android.graphics.ImageFormat.NV21, width, height, null)
}

private fun bitmapToBase64(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
}

private fun sendImageViaWebSocket(base64Image: String) {
    try {
        webSocket.send(base64Image)
        Log.d("CameraScreen", "Image sent via WebSocket")
    } catch (e: Exception) {
        Log.e("CameraScreen", "WebSocket send error", e)
    }
}