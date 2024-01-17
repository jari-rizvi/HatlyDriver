package com.teamx.hatlyDriver.ui.fragments.Dashboard.Profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentProfileBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.utils.DialogHelperClass
import com.teamx.hatlyDriver.utils.PrefHelper
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(),DialogHelperClass.Companion.DialogExitApp  {

    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        if (!MainApplication.localeManager!!.getLanguage()
                .equals(LocaleManager.Companion.LANGUAGE_ENGLISH)
        ) {

            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                       R.drawable.stripe_ic_arrow_right_circle,
                    requireActivity().theme
                )
            )

        } else {
            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.back_arrow,
                    requireActivity().theme
                )
            )

        }


        if (!mViewModel.deleteUserApi.hasActiveObservers()) {
            mViewModel.deleteUserApi.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> { loadingDialog.dismiss()
                        onToSignUpPage()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (isAdded) {
                                mViewModel.logout()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    dataStoreProvider.removeAll()

                                }

                                PrefHelper.getInstance(requireContext()).clearAll()

                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )

                                navController.navigate(R.id.logInFragment, arguments, null)


                            }
                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
            })
        }


        mViewDataBinding.imgBack.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment, arguments, options)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment, arguments, options)


                }
            })

        mViewDataBinding.btnLogout.setOnClickListener {
            mViewModel.logout()
            if (!mViewModel.logoutResponse.hasActiveObservers()) {
                mViewModel.logoutResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Resource.Status.AUTH -> {
                            loadingDialog.dismiss()
                            onToSignUpPage()
                        }

                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    dataStoreProvider.removeAll()
                                }

                                navController = Navigation.findNavController(
                                    requireActivity(),
                                    R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.logInFragment, arguments, options)
                            }
                        }

                        Resource.Status.ERROR -> {
                            loadingDialog.dismiss()
                            mViewDataBinding.root.snackbar(it.message!!)
                        }
                    }
                }
            }

        }

        mViewDataBinding.btnAccountSettings.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.editProfileFragment, arguments, options)
        }

        mViewDataBinding.btnHelpCentre.setOnClickListener {
            showToast("Coming Soon")
        }

        mViewDataBinding.btnLanguage.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.languageFragment, arguments, options)
        }
        mViewDataBinding.btnPrivacy.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://sites.google.com/view/privacy-policy-for-hatly-drive/home")
            startActivity(openURL)
        }
        mViewDataBinding.btnTerms.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://sites.google.com/view/privacy-policy-for-hatly-drive/home")
            startActivity(openURL)
        }
   /*     mViewDataBinding.btnContactUs.setOnClickListener {
            showToast("Coming Soon")
        }*/

        var dialog: Dialog? = null

        mViewDataBinding.btnContactUs.setOnClickListener {
            if (dialog == null) {
                dialog = DialogHelperClass.deleteUserDialog(
                    requireContext(), dialogCallBack = this@ProfileFragment
                )
                dialog?.show()
                dialog?.setOnDismissListener {
                    dialog = null
                }
            } else {
                dialog?.dismiss()
                dialog = null
            }


        }


        mViewDataBinding.btnWallet.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.walletFragment, arguments, options)
        }

        mViewDataBinding.imgNotification.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.notificaitonFragment, arguments, options)
        }


        mViewModel.me()
        if (!mViewModel.meResponse.hasActiveObservers()) {
            mViewModel.meResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            mViewDataBinding.userName.text = try {
                                data.name.toString()
                            } catch (e: Exception) {
                                ""
                            }
                            mViewDataBinding.userEmail.text = try {
                                data.contact.toString()
                            } catch (e: Exception) {
                                ""
                            }
                            try {

                                Picasso.get().load(data?.profileImage).resize(500, 500)
                                    .into(mViewDataBinding.profilePicture)
                            } catch (e: Exception) {

                            }


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        mViewDataBinding.root.snackbar(it.message!!)
                    }
                }
            }
        }

    }

    override fun exitAppSystem(password: String) {

        val params = JsonObject()
        params.addProperty("password", password)


        mViewModel.deleteUserApi(params)
    }

}