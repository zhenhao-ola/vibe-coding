package com.flamyoad.socketplayground.websocket

import android.content.Context
import android.widget.Toast
import io.ktor.client.*
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.*

class KtorWebSocketClient(private val context: Context, private val url: String) : WebSocketClient {
    private val client = HttpClient {
        install(WebSockets)
    }
    private var session: DefaultClientWebSocketSession? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun connect() {
        scope.launch {
            client.webSocketSession(urlString = url).let {
                session = it
                for (frame in session!!.incoming) {
                    if (frame is Frame.Text) {
                        val message = frame.readText()
                        Toast.makeText(context, "Message received: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun disconnect() {
        scope.launch {
            session?.close()
            client.close()
        }
    }

    override fun send(message: String) {
        scope.launch {
            session?.send(Frame.Text(message))
        }
    }
}
