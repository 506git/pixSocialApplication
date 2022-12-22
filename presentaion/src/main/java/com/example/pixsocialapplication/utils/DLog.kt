package com.example.pixsocialapplication.utils

import timber.log.Timber

class DLog {

    fun d(message: String?, type: String? = "") {
        Timber.tag(TAG).d(START_LINE)
        Timber.tag(TAG).d("$type")
        Timber.tag(TAG).d("$message")
        Timber.tag(TAG).d(END_LINE)
    }

    fun i(message: String?, type: String? = "") {
        Timber.tag(TAG).i(START_LINE)
        Timber.tag(TAG).i("$type")
        Timber.tag(TAG).i(" $message ")
        Timber.tag(TAG).i(END_LINE)
    }

    fun e(message: String?, type: String? = "") {
        Timber.tag(TAG).e(START_LINE)
        Timber.tag(TAG).e("$type")
        Timber.tag(TAG).e(" $message ")
        Timber.tag(TAG).e(END_LINE)
    }


    companion object {
        private const val TAG: String = "*LOGGING===>"
        private const val START_LINE: String = "================start================\n"
        private const val END_LINE: String = "\n================finish================"
    }

}
