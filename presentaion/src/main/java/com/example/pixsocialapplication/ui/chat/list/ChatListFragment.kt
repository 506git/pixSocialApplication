package com.example.pixsocialapplication.ui.chat.list

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.CustomDialogBinding
import com.example.pixsocialapplication.databinding.FragmentChatListBinding
import com.example.pixsocialapplication.model.ImageDetailModel
import com.example.pixsocialapplication.model.RoomUserInfoModel
import com.example.pixsocialapplication.service.PixPushService
import com.example.pixsocialapplication.ui.MainViewModel
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.ui.gallery.GalleryActivity
import com.example.pixsocialapplication.utils.*
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private lateinit var roomUserInfoModel: RoomUserInfoModel
    private var data = JSONObject()

    private val chatViewModel: ChatViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var chatRoomListViewAdapter: ChatRoomListViewAdapter
    private var chatListArray = arrayListOf<ChatListVO>()

    private var lastPos: Int = 0

    private lateinit var binding: FragmentChatListBinding

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    private lateinit var tts: TextToSpeech

    var bottomPos = false

    private var longClick = false
    private var mBroadCastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val data = intent?.getStringExtra("uri")
            chatViewModel.sendImageMessage(data.toString(), roomUserInfoModel.userId.toString(), roomUserInfoModel.roomId.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        roomUserInfoModel = arguments?.getParcelable("roomInfo", RoomUserInfoModel::class.java)!! <- 버전 오류
        roomUserInfoModel = arguments?.getParcelable<RoomUserInfoModel>("roomInfo")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(layoutInflater)
        val filter = IntentFilter().apply { addAction("gallery") }
        DLog.d("TEST---> ${roomUserInfoModel.toString()}","------>>>")
        data = JSONObject().apply {
            put("roomId", roomUserInfoModel.roomId)
            put("userId", roomUserInfoModel.userId)
        }

        chatRoomListViewAdapter = ChatRoomListViewAdapter(chatListArray).apply {
            setChatItemClickListener(object : ChatRoomListViewAdapter.ChatItemClickListener {
                override fun onItemClick(position: Int) {
                    if (!longClick) {
                        val chatSelected = chatListArray[position]
                        if (chatSelected.message_type == "speak")
                            tts.speak(
                                chatListArray[position].message_body,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                "uid"
                            )
                        else if (chatSelected.message_type == "image"){
                            handleEvent(event = MessageEvent.GoImageDetail(chatSelected))
                        }
//
                    } else longClick = false
                }
            })
            setChatItemLongClickListener(object :
                ChatRoomListViewAdapter.ChatItemLongClickListener {
                override fun onItemLongClick(position: Int) {
                    longClick = true
                    val chatSelected = chatListArray[position]
                    val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
                    val dialog = CommonUtils.customDialog(dialogBinding.root, context!!, true)
                    with(dialogBinding) {
                        if (chatSelected.message_type == "photo") {
                            btnPlay.setOnClickListener {
                                dialog.dismiss()
                                if (Settings.canDrawOverlays(context)) {
                                    val intent = Intent(context, PixPushService::class.java).apply {
                                        putExtra("imageUri", chatSelected.message_body)
                                    }
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        context?.startForegroundService(intent)
                                    } else {
                                        context?.startService(intent)
                                    }
                                    activity?.finish()
                                } else {
                                    startActivity(
                                        Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:${activity?.packageName}")
                                        )
                                    )
                                }
                            }
                        } else {
                            btnPlay.visibility = View.GONE
                        }
                        btnDelete.setOnClickListener {
                            dialog.dismiss()
//                        chatViewModel.removeChat(chatSelected.messageKey, roomId.toString())
                        }
                    }
                    dialog.show()
                }
            })
        }

        chatViewModel.joinRoom(data)
        chatViewModel.receiveMessage()
        chatViewModel.getRoomChatList(roomUserInfoModel.roomId.toString())

        repeatOnStarted {
            chatViewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }

        repeatOnStarted {
            chatViewModel.getRoomChatList.collect { it ->
                chatListArray = if (it == null) arrayListOf() else {
                    it.forEach { it ->
                        if (it.message_sender == "you") {
                            it.message_profile = roomUserInfoModel.roomImage.toString()
                            it.message_name = roomUserInfoModel.name.toString()
                        } else it.message_name = Config.userName
                    }
                    it as ArrayList<ChatListVO>
                }

                val checkDelete = chatRoomListViewAdapter.itemCount >= chatListArray.size

                chatRoomListViewAdapter.addAllItem(chatListArray)
                chatRoomListViewAdapter.notifyDataSetChanged()

                lastPos = if (chatRoomListViewAdapter.itemCount == 0) 0
                else chatRoomListViewAdapter.itemCount - 1

                if (!checkDelete) {
                    binding.chatList.scrollToPosition(lastPos)
                }
            }
        }

        with(binding) {
            btnPlay.setSafeOnClickListener {
                val text = editSendChat.text.toString()
                if (text.isNotEmpty()) {
                    val messageData = JSONObject().apply {
                        put("roomId", roomUserInfoModel.roomId)
                        put("userId", roomUserInfoModel.userId)
                        put("messageBody", editSendChat.text.toString())
                        put("messageType", "text")
                    }
                    chatViewModel.sendMessage(messageData)
                    editSendChat.setText("")
                } else handleEvent(MessageEvent.ShowToast("글자가 아무것도 없어요"))
            }

            btnGallery.setSafeOnClickListener {
                startActivity(Intent(context, GalleryActivity::class.java))
            }

            chatList.apply {
                adapter = chatRoomListViewAdapter
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                setHasFixedSize(true)
/*            addOnLayoutChangeListener { view, i, i2, i3, bottom, i5, i6, i7, oldBottom ->
//                if (bottom < oldBottom) {
//                    view.postDelayed(Runnable {
//                        this.smoothScrollToPosition(lastPos)
//                    }, 50)
//                }
//            }*/
            }
        }

        lastPos = (binding.chatList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        bottomPos = lastPos == chatRoomListViewAdapter.itemCount - 1

        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    DLog.e("Language not support")
                }
            } else
                DLog.e("Initialization failed")
        }).apply {
            setPitch(1.0f)
            setSpeechRate(1.25f)
        }

        activity?.registerReceiver(mBroadCastReceiver, filter)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setAppbarTitle(roomUserInfoModel.name.toString(), "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatViewModel.leaveRoom(data)
        chatListArray.clear()
        mainViewModel.setAppbarTitle("대화목록", "")
        activity?.unregisterReceiver(mBroadCastReceiver)
        mBroadCastReceiver = null
        tts.stop()
        tts.shutdown()
    }

    private fun handleEvent(event: MessageEvent) = when (event) {
        is MessageEvent.ShowToast -> CommonUtils.snackBar( activity!!, event.text, Snackbar.LENGTH_SHORT)
        is MessageEvent.OffLine -> CommonUtils.networkState = event.state
        is MessageEvent.Loading -> if (event.visible) CommonUtils.showDialog(activity!!) else CommonUtils.dismissDialog(activity!!)
        is MessageEvent.AddMessage -> {
            chatListArray.add(event.chat)
            chatRoomListViewAdapter.addItem(event.chat)
            chatRoomListViewAdapter.notifyDataSetChanged()
        }
        is MessageEvent.GoImageDetail -> {
            startActivity(Intent(context, ImageDetailActivity::class.java).apply {
                putExtra("imageInfo", ImageDetailModel(
                    userName = event.chatListVO.message_name,
                    imageUrl = event.chatListVO.message_body,
                    createdAt = event.chatListVO.createdAt
                ))
            })
        }
    }
}

///**
// * Sets up the [RecyclerView] and binds [ArticleAdapter] to it
// */
//private fun FragmentChatListBinding.bindAdapter(chatRoomAdapter: ChatRoomAdapter) {
//    chatList.adapter = chatRoomAdapter
//    chatList.layoutManager = LinearLayoutManager(chatList.context)
//    val decoration = DividerItemDecoration(chatList.context, DividerItemDecoration.VERTICAL)
//    chatList.addItemDecoration(decoration)
//}