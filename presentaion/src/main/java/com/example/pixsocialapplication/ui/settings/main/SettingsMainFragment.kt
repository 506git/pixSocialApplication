package com.example.pixsocialapplication.ui.settings.main

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.domain.model.User
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.FragmentSettingsMainBinding
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader
import com.example.pixsocialapplication.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsMainFragment : Fragment() {
    private lateinit var binding: FragmentSettingsMainBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsMainBinding.inflate(layoutInflater)



        with(binding) {
            btnTerms.setSafeOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_settingsMainFragment_to_settingDetailFragment)
            }
            btnBlockUser.setSafeOnClickListener { }
            btnUsePermission.setSafeOnClickListener { }
            btnDeleteCache.setSafeOnClickListener {
                CommonUtils.alertDialog(context!!, "캐시를 비우시겠습니까?", "확인", false,
                    onClick = DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                        settingViewModel.deleteCache(context!!)
                })

            }
            btnServiceCenter.setSafeOnClickListener { }
            btnUsePush.setSafeOnClickListener { }
            btnVersion.setSafeOnClickListener { }
            btnLogout.setSafeOnClickListener { settingViewModel.logout() }
        }

        initObserve()

        return binding.root
    }

    private fun initObserve() {
        settingViewModel.getBuildVersion(context!!)
        settingViewModel.getUserInfo()

        settingViewModel.buildVersion.observe(viewLifecycleOwner) {
            binding.btnVersion.text = "${getString(R.string.text_version)} : $it / $it"
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                settingViewModel.state.collect(){
                    val user = it.data
                    with(binding) {
                        txtUserName.text = user?.displayName ?: ""
                        txtUserDesc.text = user?.desc ?: ""
                        ImageLoader(context!!).imageCircleLoadWithURL(
                            user?.imageUrl.toString() ?: "", imgProfile
                        )
                    }
                }
            }
        }
//        settingViewModel.userInfo.observe(viewLifecycleOwner) {
//            with(binding){
//                txtUserName.text = it?.displayName?:""
//                txtUserDesc.text = it?.desc?:""
//                ImageLoader(context!!).imageCircleLoadWithURL(it?.imageUrl.toString()?:"", imgProfile)
//            }
//        }
    }
}