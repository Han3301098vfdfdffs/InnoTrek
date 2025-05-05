package com.example.innotrek.ui.screens.terminal

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

class TcpClient(
    private val ip: String,
    private val port: Int,
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionStatusChanged: (ConnectionStatus) -> Unit
) {
    private var socket: Socket? = null
    private var inputStream: BufferedReader? = null
    private var outputStream: PrintWriter? = null

    // Hilo para recepción de datos
    private var receiveThread: Thread? = null
    // Hilo para procesamiento/impresión de datos
    private var processThread: Thread? = null

    // Cola para comunicación entre hilos
    private val messageQueue = LinkedBlockingQueue<String>()
    private var running = false

    fun connect() {
        if (running) return

        running = true
        receiveThread = Thread {
            try {
                onConnectionStatusChanged(ConnectionStatus.CONNECTING)
                socket = Socket(ip, port)
                inputStream = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                outputStream = PrintWriter(socket!!.getOutputStream(), true)

                onConnectionStatusChanged(ConnectionStatus.CONNECTED)

                // Hilo de recepción
                while (running) {
                    try {
                        val message = inputStream?.readLine() ?: break
                        // Encolar mensaje para procesamiento
                        messageQueue.put(message)
                    } catch (e: IOException) {
                        if (running) {
                            onConnectionStatusChanged(ConnectionStatus.ERROR)
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                onConnectionStatusChanged(ConnectionStatus.ERROR)
            } finally {
                stopClient()
            }
        }.apply { start() }

        // Hilo de procesamiento/impresión
        processThread = Thread {
            while (running) {
                try {
                    val message = messageQueue.take()
                    // Procesar el mensaje (en este caso simplemente pasarlo al callback)
                    onMessageReceived(message)
                } catch (e: InterruptedException) {
                    // Thread interrupted, exit
                    break
                }
            }
        }.apply { start() }
    }

    fun stopClient() {
        running = false
        receiveThread?.interrupt()
        processThread?.interrupt()

        try {
            inputStream?.close()
            outputStream?.close()
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        onConnectionStatusChanged(ConnectionStatus.DISCONNECTED)
        receiveThread = null
        processThread = null
    }

    enum class ConnectionStatus {
        DISCONNECTED, CONNECTING, CONNECTED, ERROR
    }
}