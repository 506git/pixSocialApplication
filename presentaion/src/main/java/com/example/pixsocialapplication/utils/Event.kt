package com.example.pixsocialapplication.utils

sealed class Event {
    data class ShowToast(val text: String) : Event()
    data class OffLine(val state : Boolean) : Event()
}