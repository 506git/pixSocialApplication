package com.example.pixsocialapplication.ui.chat.list

import android.content.*
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
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomChat
import com.example.pixsocialapplication.databinding.CustomDialogBinding
import com.example.pixsocialapplication.databinding.FragmentChatListBinding
import com.example.pixsocialapplication.service.PixPushService
import com.example.pixsocialapplication.ui.MainViewModel
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.ui.gallery.GalleryActivity
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.setSafeOnClickListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ChatListFragment : Fragment() {

    private var email: String? = null
    private var name: String? = null
    private var roomId: String? = null

    private val chatViewModel: ChatViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var chatRoomListViewAdapter: ChatRoomListViewAdapter
    private var chatListArray = arrayListOf<RoomChat>()

    private var lastPos: Int = 0

    private lateinit var binding: FragmentChatListBinding

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    private lateinit var tts: TextToSpeech

    private var listPosition = 0

    private var longClick = false
    private var mBroadCastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val data = intent?.getStringExtra("uri")
            DLog().d("message $data")
            chatViewModel.sendImageMessage(data.toString(), roomId.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("roomTitle").toString()
        name = arguments?.getString("roomName").toString()
        roomId = arguments?.getString("roomId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(layoutInflater)

        val filter = IntentFilter().apply {
            addAction("gallery")
        }

        chatViewModel.getRoomChatList(roomId.toString())

        chatViewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) dialog.show()
            else if (!it) dialog.dismiss()
        }

        binding.btnPlay.setSafeOnClickListener {
            val text = binding.editSendChat.text.toString()
            if(text.isNotEmpty()){
                chatViewModel.sendMessage(binding.editSendChat.text.toString(), roomId.toString())
                binding.editSendChat.setText("")
            } else CommonUtils.snackBar(activity!!, "????????? ???????????? ?????????", Snackbar.LENGTH_INDEFINITE)
        }

        activity?.registerReceiver(mBroadCastReceiver, filter)

        chatViewModel.getRoomChatList.observe(viewLifecycleOwner) {
            if (it == null) {
                chatListArray = arrayListOf()
            } else chatListArray = it as ArrayList<RoomChat>

            val checkDelete = chatRoomListViewAdapter.itemCount >= chatListArray.size

            chatRoomListViewAdapter.addItem(chatListArray)
            chatRoomListViewAdapter.notifyDataSetChanged()

            lastPos = if (chatRoomListViewAdapter.itemCount == 0) 0
            else chatRoomListViewAdapter.itemCount - 1

            if(!checkDelete){
                binding.chatList.smoothScrollToPosition(lastPos)
            }

        }

        chatRoomListViewAdapter = ChatRoomListViewAdapter(chatListArray)


        chatRoomListViewAdapter.setChatItemClickListener(object :
            ChatRoomListViewAdapter.ChatItemClickListener {
            override fun onItemClick(position: Int) {
                if (!longClick) {
                    val chatSelected = chatListArray[position]
                    if (chatSelected.messageType == "speak")
                        tts.speak(
                            chatListArray[position].message.toString(),
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "uid"
                        )
                    else if (chatSelected.messageType == "photo")
                        startActivity(Intent(context, ImageDatailActivity::class.java).apply {
                            putExtra("name", chatSelected.messageSenderName)
                            putExtra("imageUri", chatSelected.message)
                        })
                } else longClick = false

            }
        })

        chatRoomListViewAdapter.setChatItemLongClickListener(object :
            ChatRoomListViewAdapter.ChatItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                longClick = true
                val chatSelected = chatListArray[position]
                val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
                val dialog = CommonUtils.customDialog(dialogBinding.root, context!!, true)
                with(dialogBinding) {
                    if (chatSelected.messageType == "photo") {
                        btnPlay.setOnClickListener {
                            dialog.dismiss()
                            if (Settings.canDrawOverlays(context)) {
                                val intent = Intent(context, PixPushService::class.java).apply {
                                    putExtra("imageUri", chatSelected.message)
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
                        chatViewModel.removeChat(chatSelected.messageKey, roomId.toString())

                    }
                }
                dialog.show()

            }
        })
        binding.chatList.apply {
            adapter = chatRoomListViewAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
//                reverseLayout = true
//                stackFromEnd = true //??????
            }
            setHasFixedSize(true)
            addOnLayoutChangeListener { view, i, i2, i3, bottom, i5, i6, i7, oldBottom ->
                if (bottom < oldBottom) {
                    view.postDelayed(Runnable {
                        this.smoothScrollToPosition(lastPos)
                    }, 50)

                }
            }
        }

//        binding.editSendChat.setOnKeyListener { view, keyCode, keyEvent ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
//
//            }
//            return@setOnKeyListener false
//        }

        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    DLog().e("Language not support")
                }
            } else
                DLog().e("Initialization failed")
        }).apply {
            setPitch(1.0f)
            setSpeechRate(1.25f)
        }

        binding.btnGallery.setSafeOnClickListener {
            startActivity(Intent(context, GalleryActivity::class.java))
        }

        return binding.root
    }

    val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference() = SNAP_TO_END
        }
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.setAppbarTitle(name.toString(), email.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setAppbarTitle("????????????", "")
        activity?.unregisterReceiver(mBroadCastReceiver)
        mBroadCastReceiver = null
        tts.stop()
        tts.shutdown()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                }
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