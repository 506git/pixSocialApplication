package com.example.pixsocialapplication.utils

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar

fun handleEvent (event: Event, context: Context) = when (event) {
    is Event.ShowToast -> CommonUtils.snackBar(context as Activity, event.text, Snackbar.LENGTH_SHORT)
    else -> {}
}
