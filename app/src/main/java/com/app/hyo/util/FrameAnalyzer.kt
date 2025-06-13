package com.app.hyo.util
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.app.hyo.util.ImageUtils.rotateBitmap
import com.app.hyo.util.ImageUtils.toYuvImage
import com.app.hyo.util.WebSocketManager.sendImage
import java.io.ByteArrayOutputStream

internal class FrameAnalyzer : ImageAnalysis.Analyzer {
    private var lastSent = 0L
    private val intervalMs = 150L
    private val jpegStream = ByteArrayOutputStream()

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

        jpegStream.reset()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 40, jpegStream)
        val jpegBytes = jpegStream.toByteArray()

        val originalBitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
        val rotatedBitmap = rotateBitmap(originalBitmap, image.imageInfo.rotationDegrees)

        sendImage(rotatedBitmap)

        lastSent = now
        image.close()
    }
}