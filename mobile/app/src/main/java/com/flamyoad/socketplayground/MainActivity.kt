package com.flamyoad.socketplayground

import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flamyoad.socketplayground.websocket.WebSocketClient
import com.flamyoad.socketplayground.websocket.OkHttpWebSocketClient
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize WebSocket client (using OkHttp implementation here)
        webSocketClient = OkHttpWebSocketClient(this, "ws://10.5.20.242:8080/ws")
        webSocketClient.connect()

        // Find the button and set up a click listener
        val sendButton: Button = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            val payload = createDeviceInfoPayload()
            webSocketClient.send(payload)
        }
    }

    private fun createDeviceInfoPayload(): String {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        // Placeholder for CPU usage (replace with actual implementation if needed)
        val cpuUsage = "N/A"

        val json = JSONObject()
        json.put("batteryLevel", batteryLevel)
        json.put("cpuUsage", cpuUsage)
        return json.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.disconnect()
    }
}