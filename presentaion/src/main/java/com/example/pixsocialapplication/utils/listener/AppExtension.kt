package com.example.pixsocialapplication.utils

import android.view.View
import com.example.pixsocialapplication.utils.listener.SafeClickListener


/**
 * 짧은시간 버튼 연타를 막기위한 처리.
 * 버튼 클릭 이벤트에는 이 함수를 적극적으로 이용해야 함.
 */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    setOnClickListener(SafeClickListener {
        onSafeClick(it)
    })
}