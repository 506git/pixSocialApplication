package com.example.data.socket

import com.example.domain.core.Result
import com.example.domain.socket.AppSocket
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject

class SocketManager(private val mSocket: Socket) : AppSocket {

    val isConnected : Boolean get() = mSocket.connected()

    init {
        mSocket.on(Socket.EVENT_CONNECT) {

        }?.on(Socket.EVENT_DISCONNECT) { args ->

        }?.on(Socket.EVENT_CONNECT_ERROR) { args ->

        }
    }

    override fun connect() {
        mSocket.open()
    }

    override fun disconnect() {
        mSocket.off("sendMessage")
        mSocket.off("receiveMessage")
        mSocket.disconnect()
//        mSocket.close()
    }

    fun on(name: String, listener: Emitter.Listener) = callbackFlow<Unit> {

    }

    override fun emit (name : String, any: Any) {
        mSocket.emit(name, any)
    }

    override fun emit (name : String, json: JSONObject) {
        mSocket.emit(name, json)
    }

    override fun on(name: String, listener: Any) {
//        Emitter.Listener =
        mSocket.on(name, listener as Emitter.Listener)
    }

}