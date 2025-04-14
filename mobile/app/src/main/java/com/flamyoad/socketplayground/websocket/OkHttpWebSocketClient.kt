package com.flamyoad.socketplayground.websocket

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class OkHttpWebSocketClient(private val context: Context, private val url: String) : WebSocketClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    override fun connect() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Connected to $url")
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WebSocket", "Received message: $text")
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Message received: $text", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("WebSocket", "Error: ${t.message}")
            }
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebSocket", "Closed: $code / $reason")
            }
        })
    }

    override fun disconnect() {
        webSocket?.close(1000, "Closing connection")
        webSocket = null
    }

    override fun send(message: String) {
        webSocket?.send(message) // Implemented send method
    }
}
