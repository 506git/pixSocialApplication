package com.example.pixsocialapplication.ui.chat.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomInfo
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentChatRoomBinding
import com.example.pixsocialapplication.ui.MainViewModel
import com.example.pixsocialapplication.ui.adapter.ArticleAdapter
import com.example.pixsocialapplication.ui.adapter.TestAdapter
import com.example.pixsocialapplication.ui.adapter.UserRoomListViewAdapter
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.utils.DLog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentChatRoomBinding
    private lateinit var userRoomListViewAdapter : UserRoomListViewAdapter

    private val chatRoomViewModel : ChatRoomViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var roomArray = arrayListOf<RoomInfo>()

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(layoutInflater)
        chatRoomViewModel.getRoomListRepos()

        chatRoomViewModel.getRoomList.observe(viewLifecycleOwner){
            if (it == null){
                roomArray = arrayListOf()
            } else roomArray = it as ArrayList<RoomInfo>

            userRoomListViewAdapter.addItem(roomArray)
            userRoomListViewAdapter.notifyDataSetChanged()
        }

        chatRoomViewModel.loadingState.observe(viewLifecycleOwner){
            if (it) dialog.show()
            else if (!it) dialog.dismiss()
        }

        userRoomListViewAdapter = UserRoomListViewAdapter(roomArray)

        binding.chatList.apply {
            adapter = userRoomListViewAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
        }

        LinearSnapHelper().apply {
            attachToRecyclerView(binding.chatList)
        }

        userRoomListViewAdapter.setRoomItemClickListener(object : UserRoomListViewAdapter.RoomItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = bundleOf(
                    "roomTitle" to roomArray[position].room_title,
                    "roomName" to roomArray[position].room_name,
                    "roomId" to roomArray[position].room_id
                )
//                val bundle = Bundle().apply {
//                    this.putSerializable("roomInfo", roomArray[position])
//                }
                view?.findNavController()?.navigate(R.id.action_chatRoomFragment_to_chatListFragment, bundle)
            }
        })



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

    override fun onResume() {
        super.onResume()
//        mainViewModel.setFabVisible(View.VISIBLE)
    }

    override fun onPause() {
        super.onPause()
//        mainViewModel.setFabVisible(View.GONE)
        mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_HIDDEN)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatRoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

/**
 * Sets up the [RecyclerView] and binds [ArticleAdapter] to it
 */
//private fun FragmentChatRoomBinding.bindAdapter(testAdapter: TestAdapter) {
//    chatList.adapter = testAdapter
//    chatList.layoutManager = LinearLayoutManager(chatList.context)
//    val decoration = DividerItemDecoration(chatList.context, DividerItemDecoration.VERTICAL)
//    chatList.addItemDecoration(decoration)
//}