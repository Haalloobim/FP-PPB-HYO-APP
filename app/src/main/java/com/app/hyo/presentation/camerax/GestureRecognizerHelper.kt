package com.app.hyo.presentation.camerax // Make sure this package name matches yours

import YuvToRgbConverter // Ensure you have this import if it's in the same package
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult

class GestureRecognizerHelper(
    val context: Context,
    var runningMode: RunningMode = RunningMode.LIVE_STREAM,
    val gestureRecognizerListener: GestureRecognizerListener? = null
) {
    private var gestureRecognizer: GestureRecognizer? = null
    private val yuvToRgbConverter = YuvToRgbConverter(context)
    private var bitmapBuffer: Bitmap? = null

    init {
        setupGestureRecognizer()
    }

    private fun setupGestureRecognizer() {
        // Ensure you are using the .task file
        val modelName = "model/gesture_recognizer.task"

        val baseOptionBuilder = BaseOptions.builder()
            .setDelegate(Delegate.CPU)
            .setModelAssetPath(modelName)

        try {
            val optionsBuilder = GestureRecognizer.GestureRecognizerOptions.builder()
                .setBaseOptions(baseOptionBuilder.build())
                .setRunningMode(runningMode)
                .setResultListener(this::returnLivestreamResult)
                .setErrorListener(this::returnLivestreamError)

            gestureRecognizer = GestureRecognizer.createFromOptions(context, optionsBuilder.build())
        } catch (e: Exception) {
            gestureRecognizerListener?.onError("Error initializing gesture recognizer: ${e.message}")
            Log.e("GestureRecognizerHelper", "Error: ${e.message}")
        }
    }

    @OptIn(ExperimentalGetImage::class)
    fun recognizeLiveStream(imageProxy: ImageProxy) {
        val frameTime = SystemClock.uptimeMillis()

        if (bitmapBuffer == null) {
            bitmapBuffer = Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
        }

        yuvToRgbConverter.yuvToRgb(imageProxy.image!!, bitmapBuffer!!)

        val matrix = Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
        }
        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer!!, 0, 0, bitmapBuffer!!.width, bitmapBuffer!!.height, matrix, true
        )

        val mpImage = BitmapImageBuilder(rotatedBitmap).build()
        gestureRecognizer?.recognizeAsync(mpImage, frameTime)
    }

    private fun returnLivestreamResult(result: GestureRecognizerResult, input: MPImage) {
        gestureRecognizerListener?.onResults(result)
    }

    private fun returnLivestreamError(error: RuntimeException) {
        gestureRecognizerListener?.onError(error.message ?: "An unknown error has occurred")
    }

    interface GestureRecognizerListener {
        fun onError(error: String)
        fun onResults(result: GestureRecognizerResult)
    }
}