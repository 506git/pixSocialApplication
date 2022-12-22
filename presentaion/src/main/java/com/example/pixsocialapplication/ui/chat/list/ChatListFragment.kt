package com.example.pixsocialapplication.ui.chat.list

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomChat
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentChatListBinding
import com.example.pixsocialapplication.ui.MainViewModel
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.ui.adapter.UserRoomListViewAdapter
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.ui.gallery.GalleryActivity
import com.example.pixsocialapplication.utils.DLog
import com.example.ssolrangapplication.common.setSafeOnClickListener
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
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

    private var lastPos : Int = 0

    private lateinit var binding: FragmentChatListBinding
    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    private lateinit var tts : TextToSpeech

    private var mBroadCastReceiver : BroadcastReceiver? = object : BroadcastReceiver(){
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

        chatViewModel.getRoomChatList(roomId.toString())

        chatViewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) dialog.show()
            else if (!it) dialog.dismiss()
        }

//        chatViewModel.itemSelectedPos.observe(viewLifecycleOwner){
//        }

        binding.btnPlay.setSafeOnClickListener {
            DLog().d("test")
        }
        val filter = IntentFilter().apply {
            addAction("gallery")
        }
        activity?.registerReceiver(mBroadCastReceiver, filter)

        chatViewModel.getRoomChatList.observe(viewLifecycleOwner) {
//            Timber.tag("PRETTY LOGGING").d("test${it}")
            if (it == null) {
                chatListArray = arrayListOf()
            } else chatListArray = it as ArrayList<RoomChat>

            chatRoomListViewAdapter.addItem(chatListArray)
            chatRoomListViewAdapter.notifyDataSetChanged()

            lastPos = if (chatRoomListViewAdapter.itemCount == 0) 0
            else chatRoomListViewAdapter.itemCount - 1

            binding.chatList.smoothScrollToPosition(lastPos)
        }

        chatRoomListViewAdapter = ChatRoomListViewAdapter(chatListArray)


        chatRoomListViewAdapter.setChatItemClickListener(object : ChatRoomListViewAdapter.ChatItemClickListener {
            override fun onItemClick(position: Int) {
                val chatSelected = chatListArray[position]
                if(chatSelected.messageType == "speak")
                    tts.speak(chatListArray[position].message.toString(),TextToSpeech.QUEUE_FLUSH,null,"uid")
                else if (chatSelected.messageType == "photo")
                    startActivity(Intent(context,ImageDatailActivity::class.java).apply {
                        putExtra("name", chatSelected.messageSenderName)
                        putExtra("imageUri", chatSelected.message) })
            }

        })
        binding.chatList.apply {
            adapter = chatRoomListViewAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            addOnLayoutChangeListener { view, i, i2, i3, bottom, i5, i6, i7, oldBottom ->
                if(bottom < oldBottom) {
                    view.postDelayed(Runnable {
                        this.smoothScrollToPosition(lastPos)
                    },50)

                }
            }
        }

        binding.editSendChat.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                chatViewModel.sendMessage(binding.editSendChat.text.toString(), roomId.toString())
                binding.editSendChat.setText("")
            }
            return@setOnKeyListener false
        }

        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                    DLog().e("Language not support")
                }
            } else
                DLog().e("Initialization failed")
        }).apply {
            setPitch(1.0f)
            setSpeechRate(1.25f)
        }

        binding.btnGallery.setSafeOnClickListener {
            startActivity(Intent(context,GalleryActivity::class.java).apply {

            })
//            view?.findNavController()?.navigate(R.id.action_chatListFragment_to_galleryFragment)
        }



        return binding.root
    }

    val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference() = SNAP_TO_END
        }
    }

/*    private val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
    private val SCOPES = listOf<String>(MESSAGING_SCOPE)
    private val PROJECT_ID = "pixsocial-a41c6"
    private val BASE_URL = "https://fcm.googleapis.com"
    private val FCM_SEND_ENDPOINT = "/v1/projects/$PROJECT_ID/messages:send"

    @Throws(IOException::class)
    fun getAccessToken(context: Context) : String {
        val assertmanager : AssetManager = context.assets
        val fileDescriptor : AssetFileDescriptor = assertmanager.openFd("fcmdemo.json")
        val fileInputStream : FileInputStream = fileDescriptor.createInputStream()
        val googleCredentials: GoogleCredentials =
            GoogleCredentials.fromStream(fileInputStream).createScoped(SCOPES)
        return googleCredentials.refreshAccessToken().tokenValue
    }

    fun SendNotify(msg: String, url :String){
        CoroutineScope(Dispatchers.IO).launch {
            val root = JSONObject()
            val notification = JSONObject()
            notification.put("body", "test")
            notification.put("title", "test")
            root.put("notification", notification)
            root.put("to", "c7kfyE1tS2uq8SP3AwtoUe:APA91bE4-Dd1T6AZil_Yxj0JaH-9Wr995PTrWHXT0uOMAMEG6CTgpTvg0a5k9cZPc8Wut3dBOZvyvM4QhorSwaFkBsFWKxOAPWmzXLAdG1-c45-YYy_aRdlsVzCx4KkH3zYTEorurEbG")

            val Url = URL("$BASE_URL$FCM_SEND_ENDPOINT")
            val conn = Url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.doInput = true
            conn.addRequestProperty("Authorization", "key=${getAccessToken(context!!)}")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Content-type", "application/json")
            val os = conn.outputStream
            os.write(root.toString().toByteArray(charset("utf-8")))
            os.flush()
            conn.responseCode
        }
    }
*/
    override fun onResume() {
        super.onResume()

        mainViewModel.setAppbarTitle(name.toString(), email.toString())
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setAppbarTitle("대화목록", "")
        activity?.unregisterReceiver(mBroadCastReceiver)
        mBroadCastReceiver = null;
        tts?.stop()
        tts?.shutdown()
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