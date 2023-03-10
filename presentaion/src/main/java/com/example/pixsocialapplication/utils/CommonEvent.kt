package com.example.pixsocialapplication.utils

import com.example.domain.vo.ChatListVO

sealed class CommonEvent {
    data class ShowToast(val text: String) : CommonEvent()
    data class OffLine(val state : Boolean) : CommonEvent()
    data class Loading(val visible: Boolean) : CommonEvent()

    data class GoMain(val state : Boolean) : CommonEvent()
}

sealed class MainEvent {
    data class ShowToast(val text: String) : MainEvent()
    data class OffLine(val state : Boolean) : MainEvent()
    data class Loading(val visible: Boolean) : MainEvent()

    data class AppbarTitle(val title : String)  : MainEvent()

    data class BottomBehavior(val state : Int) : MainEvent()

}

sealed class MessageEvent {
    data class ShowToast(val text: String) : MessageEvent()
    data class OffLine(val state : Boolean) : MessageEvent()
    data class Loading(val visible: Boolean) : MessageEvent()

    data class AddMessage(val chat : ChatListVO) : MessageEvent()

    data class GoImageDetail(val chatListVO : ChatListVO) : MessageEvent()

}
