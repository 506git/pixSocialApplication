package com.example.pixsocialapplication.ui.settings.main

import android.content.DialogInterface
import android.content.Intent
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
import com.example.pixsocialapplication.ui.intro.LogInActivity
import com.example.pixsocialapplication.utils.*
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
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
                handleEvent(SettingsEvent.GoToNav(R.id.action_settingsMainFragment_to_settingDetailFragment))
            }
            btnBlockUser.setSafeOnClickListener { }
            btnUsePermission.setSafeOnClickListener { }
            btnDeleteCache.setSafeOnClickListener {
                CommonUtils.alertDialog(context!!, "캐시를 비우시겠습니까?", "확인", false,
                    onClick = DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                        handleEvent(SettingsEvent.ClearCache)
                    })
            }
            btnServiceCenter.setSafeOnClickListener { }
            btnUsePush.setSafeOnClickListener { }
            btnVersion.setSafeOnClickListener { }
            btnLogout.setSafeOnClickListener {
                handleEvent(SettingsEvent.SignOut)
            }
        }

        initObserve()

        return binding.root
    }

    private fun initObserve() {
        settingViewModel.getBuildVersion(context!!)
        settingViewModel.getUserInfo()

        repeatOnStarted {
            settingViewModel.eventFlow.collect() { event ->
                handleEvent(event)
            }
        }

        repeatOnStarted {
            settingViewModel.userInfo.collect() {
                with(binding) {
                    txtUserName.text = it.displayName ?: ""
                    txtUserDesc.text = it.desc ?: ""
                    ImageLoader(context!!).imageCircleLoadWithURL(
                        it.imageUrl ?: "", imgProfile
                    )
                }
            }
        }

    }

    private fun handleEvent(event: SettingsEvent) = when (event) {
        is SettingsEvent.ShowToast -> CommonUtils.snackBar(
            activity!!,
            event.text,
            Snackbar.LENGTH_SHORT
        )
        is SettingsEvent.OffLine -> CommonUtils.networkState = event.state
        is SettingsEvent.Loading -> if (event.visible) CommonUtils.showDialog(activity!!) else CommonUtils.dismissDialog(
            activity!!
        )
        is SettingsEvent.SignOut -> {
            settingViewModel.logout()
            activity?.finish()
            startActivity(Intent(activity, LogInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
        is SettingsEvent.ClearCache -> {
            try {
                context?.cacheDir?.deleteRecursively()
            } catch (e: Exception) {
                DLog.d(e.toString())
            }
        }
        is SettingsEvent.Version -> {
            binding.btnVersion.text = "${getString(R.string.text_version)} :  / ${event.version}"
        }
        is SettingsEvent.GoToNav -> {
            view?.findNavController()?.navigate(event.destination)
        }
    }
}