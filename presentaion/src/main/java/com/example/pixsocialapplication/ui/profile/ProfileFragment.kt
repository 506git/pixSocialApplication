package com.example.pixsocialapplication.ui.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentProfileBinding
import com.example.pixsocialapplication.ui.friends.FriendsViewModel
import com.example.pixsocialapplication.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "userId"
private const val ARG_PARAM2 = "userName"
private const val ARG_PARAM3 = "userImage"
private const val ARG_PARAM4 = "userEmail"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var userId: String? = null
    private var userName: String? = null
    private var userImage: String? = null
    private var userEmail: String? = null

    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private val profileViewModel : ProfileViewModel by viewModels()

    private lateinit var binding : FragmentProfileBinding

//    override fun onStart() {
//        super.onStart()
//        val params = dialog?.window?.attributes
//        params!!.apply {
//            width = WindowManager.LayoutParams.MATCH_PARENT
//            height = (CommonUtils.getScreenHeight(context!!) * 0.3).toInt()
//        }둥글게
//        dialog?.window?.attributes = params
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val height = (CommonUtils.getScreenHeight(context!!) * 0.35).toInt()
        binding.viewBottom.apply {
            layoutParams.height = (CommonUtils.getScreenHeight(context!!) * 0.75).toInt()
        }
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.peekHeight = height
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        arguments?.let {
            userId = it.getString(ARG_PARAM1)
            userName = it.getString(ARG_PARAM2)
            userImage = it.getString(ARG_PARAM3)
            userEmail = it.getString(ARG_PARAM4)
        }

        with(binding){
            ImageLoader(context!!).imageLoadWithUrlToBlur(userImage.toString(), imgUserBackground)
            ImageLoader(context!!).imageCircleLoadWithURL(userImage.toString(), imgUserProfile)
            txtUserName.text = userName
            txtUserDesc.text = userEmail
            viewChatStart.setSafeOnClickListener {
                profileViewModel.chatRoomStart(userId.toString())
                Toast.makeText( context, userId, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}