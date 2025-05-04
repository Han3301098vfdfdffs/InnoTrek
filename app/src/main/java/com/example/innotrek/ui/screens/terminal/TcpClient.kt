package com.example.innotrek.ui.screens.terminal

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class TcpClient(
    private val ip: String,
    private val port: Int,
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionStatusChanged: (ConnectionStatus) -> Unit
) {
    private var socket: Socket? = null
    private var running = false

    enum class ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }

    fun connect(){
        Thread{
            try{
                onConnectionStatusChanged(ConnectionStatus.CONNECTING)
                socket = Socket(ip, port)
                running = true
                onConnectionStatusChanged(ConnectionStatus.CONNECTED)

                val input = BufferedReader(InputStreamReader(socket?.getInputStream()))

                while (running) {
                    val message = input.readLine()
                    if(message != null){
                        println("DATO RECIBIDO: $message")
                        onMessageReceived(message)
                    } else {
                        stopClient()
                    }
                }
            } catch (e: Exception){
                println("ERROR en TcpClient: ${e.message}")
                onConnectionStatusChanged(ConnectionStatus.ERROR)
                e.printStackTrace()
            } finally {
                stopClient()
            }
        }.start()
    }

    fun stopClient() {
        running = false
        try {
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onConnectionStatusChanged(ConnectionStatus.DISCONNECTED)
    }
}