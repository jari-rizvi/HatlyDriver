package com.teamx.hatly.ui.fragments.Auth.newPassword

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentNewPassBinding
import com.teamx.hatly.utils.DialogHelperClass
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class CreateNewPassFragment : BaseFragment<FragmentNewPassBinding, CreateNewPassViewModel>(),
    DialogHelperClass.Companion.ChangePasswordDialog {

    override val layoutId: Int
        get() = R.layout.fragment_new_pass
    override val viewModel: Class<CreateNewPassViewModel>
        get() = CreateNewPassViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private var newPass: String? = null
    private var phone: String? = null
    private var token: String? = null
    private lateinit var options: NavOptions

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

        mViewDataBinding.btnSave.setOnClickListener {
            validate()

        }

    }

    private fun resetPassCall() {
        super.subscribeToNetworkLiveData()

        val bundle = arguments
        if (bundle != null) {
            newPass = mViewDataBinding.etPass.text.toString()
            phone = bundle.getString("phone").toString()
            Log.d("TAG", "resetPassCall: $phone")
        }

        val params = JsonObject()
        try {
            params.addProperty("password", newPass)
            params.addProperty("phoneNumber", phone)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.resetPassPhone(params, this)


        mViewModel.resetPassPhoneResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        DialogHelperClass.changePasswordDialog(requireContext(),this,true)

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })
    }

    fun validate(): Boolean {
        if (mViewDataBinding.etPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.etPass.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (mViewDataBinding.etCnfrmPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.etCnfrmPass.text.toString().trim().length < 7) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (!mViewDataBinding.etPass.text.toString().trim()
                .equals(mViewDataBinding.etCnfrmPass.text.toString().trim())
        ) {
            mViewDataBinding.root.snackbar(getString(R.string.password_does_not_match))
            return false
        }
        resetPassCall()
        return true
    }

    override fun onLoginButton() {
        navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )
        navController.navigate(R.id.homeFragment, null, options)
    }
}