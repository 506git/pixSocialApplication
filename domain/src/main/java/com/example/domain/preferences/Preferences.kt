package com.example.domain.preferences

interface Preferences {
    fun saveIntroViewStatus(show : Boolean)
    fun loadIntroViewStatus() : Boolean
    fun setStringPreferences(key : String, value: String)
    fun getStringPreferences(key:String) : String

    companion object {
        const val INTRO_NOT_USE = "intro_not_use"
    }
}