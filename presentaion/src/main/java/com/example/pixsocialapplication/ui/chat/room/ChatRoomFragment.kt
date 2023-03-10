package com.example.pixsocialapplication.ui.chat.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.domain.model.RoomListInfo
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentChatRoomBinding
import com.example.pixsocialapplication.model.RoomUserInfoModel
import com.example.pixsocialapplication.ui.MainViewModel
import com.example.pixsocialapplication.ui.adapter.UserRoomListViewAdapter
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.utils.CommonEvent
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {
    private lateinit var binding: FragmentChatRoomBinding
    private lateinit var userRoomListViewAdapter: UserRoomListViewAdapter

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var roomArray = arrayListOf<RoomListInfo>()

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(layoutInflater)

        userRoomListViewAdapter = UserRoomListViewAdapter(roomArray).apply {
            setRoomItemClickListener(object : UserRoomListViewAdapter.RoomItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val bundle = Bundle().apply {
                        putParcelable("roomInfo", RoomUserInfoModel(
                            roomId = roomArray[position]._id,
                            userId = Config.userId,
                            roomImage = roomArray[position].room_image,
                            name = roomArray[position].room_name
                        ))
                    }
                    view.findNavController().navigate(R.id.action_chatRoomFragment_to_chatListFragment, bundle)
                }
            })
        }

        with(binding) {
            chatList.apply {
                adapter = userRoomListViewAdapter
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                setHasFixedSize(true)
            }
            LinearSnapHelper().apply {
                attachToRecyclerView(chatList)
            }
        }

        chatRoomViewModel.getRoomListRepos()

        repeatOnStarted {
            chatRoomViewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        repeatOnStarted {
            chatRoomViewModel.getRoomList.collect {
                roomArray = if (it == null) arrayListOf() else it as ArrayList<RoomListInfo>

                userRoomListViewAdapter.addItem(roomArray)
                userRoomListViewAdapter.notifyDataSetChanged()
            }
        }
/*       val items = chatRoomViewModel.pagingData
//        val testAdapter = TestAdapter()
//        binding.bindAdapter(testAdapter = testAdapter)
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                items.collectLatest {
//                    testAdapter.submitData(it)
//
//                }
//            }
//        } test paging*/

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_HIDDEN)
    }

    private fun handleEvent(event: CommonEvent) = when (event) {
        is CommonEvent.ShowToast -> CommonUtils.snackBar(activity!!, event.text, Snackbar.LENGTH_SHORT)
        is CommonEvent.OffLine -> CommonUtils.networkState = event.state
        is CommonEvent.Loading -> if (event.visible) CommonUtils.showDialog(activity!!) else CommonUtils.dismissDialog(activity!!)
        else -> {}
    }
}