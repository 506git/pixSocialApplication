package com.example.pixsocialapplication.ui.friends;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.FriendInfo
import com.example.pixsocialapplication.databinding.FragmentFriendsBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.ui.adapter.FriendsAdapter
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.ui.profile.ProfileFragment
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    private var friendsArray = arrayListOf<FriendInfo>()
    private lateinit var friendsAdapter : FriendsAdapter

    private val friendsViewModel : FriendsViewModel by viewModels()

    private val dialog by lazy {
        LoadingDialog(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(layoutInflater)

        friendsAdapter = FriendsAdapter(friendsArray).apply {
            setFriendItemClickListener(object : FriendsAdapter.FriendItemClickListener {
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
        }

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
            friendsViewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        repeatOnStarted {
            friendsViewModel.getFriendList.collect{
                friendsArray = if (it == null) arrayListOf() else it as ArrayList<FriendInfo>
                friendsAdapter.addItem(friendsArray)
                friendsAdapter.notifyDataSetChanged()
            }
        }

        friendsViewModel.getFriendsListRepos()

        return binding.root
    }

    private fun handleEvent(event: FriendsViewModel.Event) = when (event) {
        is FriendsViewModel.Event.ShowToast -> CommonUtils.snackBar(activity!!, event.text, Snackbar.LENGTH_SHORT)
        is FriendsViewModel.Event.OffLine -> CommonUtils.networkState = event.state
        is FriendsViewModel.Event.Loading -> if (event.visible) CommonUtils.showDialog(activity!!) else   CommonUtils.dismissDialog(activity!!)
    }
}