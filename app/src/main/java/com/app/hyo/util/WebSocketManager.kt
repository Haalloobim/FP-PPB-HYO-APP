package com.app.hyo.util

import android.graphics.Bitmap
import android.util.Log
import com.app.hyo.presentation.camerax.PredictViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.ByteArrayOutputStream

object WebSocketManager {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun init(viewModel: PredictViewModel) {
        val request = Request.Builder()
            .url("https://a7b2-103-119-141-209.ngrok-free.app/")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received: $text")
                viewModel.updateState(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error", t)
            }
        })
    }

    fun sendImage(bitmap: Bitmap) {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream)
            val byteString = ByteString.of(*stream.toByteArray())
            webSocket.send(byteString)
            Log.d("WebSocket", "Image sent")
        } catch (e: Exception) {
            Log.e("WebSocket", "Send failed", e)
        }
    }
}
