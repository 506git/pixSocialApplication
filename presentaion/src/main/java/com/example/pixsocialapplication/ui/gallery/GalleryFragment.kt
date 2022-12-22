
package com.example.pixsocialapplication.ui.gallery

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.RoomInfo
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentGalleryBinding
import com.example.pixsocialapplication.databinding.ItemSampleBinding
import com.example.pixsocialapplication.ui.adapter.GalleryListViewAdapter
import com.example.pixsocialapplication.ui.adapter.UserRoomListViewAdapter
import com.example.pixsocialapplication.ui.chat.room.ChatRoomViewModel
import com.example.pixsocialapplication.ui.common.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class GalleryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGalleryBinding

    private val galleryViewModel : GalleryViewModel by viewModels()
    private lateinit var galleryListViewAdapter : GalleryListViewAdapter

    private var galleryArray = arrayListOf<Uri>()

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
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)

        galleryViewModel.getImageList()

        galleryViewModel.getGalleryList.observe(viewLifecycleOwner){
            if (it == null){
                galleryArray = arrayListOf()
            } else galleryArray = it as ArrayList<Uri>

            galleryListViewAdapter.addItem(galleryArray)
            galleryListViewAdapter.notifyDataSetChanged()
        }
        galleryListViewAdapter = GalleryListViewAdapter(galleryArray)

        binding.listGallery.apply {
            adapter = galleryListViewAdapter
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
        }

        galleryViewModel.loadingState.observe(viewLifecycleOwner){
            if (it) dialog.show()
            else if (!it) dialog.dismiss()
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
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}