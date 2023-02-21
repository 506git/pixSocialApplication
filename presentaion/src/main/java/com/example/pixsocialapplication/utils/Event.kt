package com.example.pixsocialapplication.utils

sealed class Event {
    data class ShowToast(val text: String) : Event()
    data class Aaa(val value: String) : Event()
    data class Bbb(val value: Int) : Event()
}