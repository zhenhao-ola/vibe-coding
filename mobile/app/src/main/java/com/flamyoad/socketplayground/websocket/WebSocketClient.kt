package com.flamyoad.socketplayground.websocket

interface WebSocketClient {
    fun connect()
    fun disconnect()
    fun send(message: String) // Added send method
}
