package com.example.pixsocialapplication.ui.friends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.FriendInfo
import com.example.domain.model.RoomInfo

import com.example.pixsocialapplication.R;
import com.example.pixsocialapplication.databinding.FragmentChatRoomBinding;
import com.example.pixsocialapplication.databinding.FragmentFriendsBinding
import com.example.pixsocialapplication.ui.adapter.FriendsAdapter
import com.example.pixsocialapplication.ui.adapter.UserRoomListViewAdapter
import com.example.pixsocialapplication.ui.chat.room.ChatRoomViewModel
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.ui.profile.ProfileFragment
import com.example.pixsocialapplication.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    private var friendsArray = arrayListOf<FriendInfo>()
    private lateinit var friendsAdapter : FriendsAdapter

    private val friendsViewModel : FriendsViewModel by viewModels()

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFriendsBinding.inflate(layoutInflater)

        friendsAdapter = FriendsAdapter(friendsArray)
        friendsAdapter.setFriendItemClickListener(object : FriendsAdapter.FriendItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                ProfileFragment().apply {
                    arguments = bundleOf(
                        "userId" to friendsArray[position]._id,
                        "userName" to friendsArray[position].name,
                        "userImage" to friendsArray[position].picture,
                        "userEmail" to friendsArray[position].email
                    )
                }.show(activity!!.supportFragmentManager,"profile")
            }
        })
        with(binding){
            listFriends.apply {
                adapter = friendsAdapter
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                setHasFixedSize(true)
            }
        }

        repeatOnStarted {
            friendsViewModel.getRoomList.collect{
                if (it?.size == 0) {
                    friendsArray = arrayListOf()
                } else
                    friendsArray = it as ArrayList<FriendInfo>

                friendsAdapter.addItem(friendsArray)
                friendsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnStarted {
            friendsViewModel.loadingState.collect {
                if (it) dialog.show()
                else if (!it) dialog.dismiss()
            }
        }

        friendsViewModel.getFriendsListRepos()

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {

            }
    }
}