package com.example.domain.socket

import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject

interface AppSocket {

    fun connect()

    fun disconnect()

    fun emit (name : String, any: Any)

    fun emit (name : String, json: JSONObject)

    fun on (name: String, listener: Any)

}