package com.teamx.hatly.ui.fragments.Auth.singup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentSignupBinding
import com.teamx.hatly.utils.DialogHelperClass
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding, SignupViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_signup
    override val viewModel: Class<SignupViewModel>
        get() = SignupViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private var userEmail: String? = null
    private var password: String? = null
    private var name: String? = null
    private var userNumber: String? = null


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

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnSignup.setOnClickListener {
            isValidate()
        }

        mViewDataBinding.btnLogin.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.logInFragment, null, options)
        }




    }

    private fun initialization() {
        name = mViewDataBinding.etName.text.toString().trim()
        userEmail = mViewDataBinding.etEmail.text.toString().trim()
        password = mViewDataBinding.etPass.text.toString().trim()
        userNumber = mViewDataBinding.etPhone.text.toString().trim()

    }

    fun signup() {

        initialization()

        if (userNumber!!.isNotEmpty() || password!!.isNotEmpty() || name!!.isNotEmpty() || userEmail!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("name", name.toString())
                params.addProperty("contact", userNumber.toString())
                params.addProperty("password", password.toString())
                params.addProperty("email", userEmail.toString())
                Log.e("UserData", params.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewModel.signup(params)

            mViewModel.signupResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            val bundle = Bundle()
                            bundle.putString("phone", data.contact)
                            bundle.putString("otpid", data._id)

                            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            navController.navigate(R.id.otpFragment, bundle, options)


                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.signupResponse.removeObservers(viewLifecycleOwner)
                }
            }
        }
    }

    private fun isValidate(): Boolean {

        if (mViewDataBinding.etName.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.enter_name))
            return false
        }
        if (mViewDataBinding.etName.text.toString().trim().length < 3) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.name_must_have_atleast_3_character_long))
            return false
        }

        if (mViewDataBinding.etName.text.toString().trim().length > 15) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.name_maximum))
            return false
        }
        if (mViewDataBinding.etEmail.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.enter_email))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mViewDataBinding.etEmail.text.toString().trim())
                .matches()
        ) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.invalid_email))
            return false
        }
        if (mViewDataBinding.etPhone.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.enter_your_password))
            return false
        }
        if (mViewDataBinding.etPhone.text.toString().trim().startsWith("0")) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.enter_Number_with_Country_Code))
            return false
        }
        if (mViewDataBinding.etPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.enter_your_password))
            return false
        }
        if (mViewDataBinding.etPass.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(com.teamx.hatly.R.string.password_8_character))
            return false
        }

        signup()

//        if (mViewDataBinding.cbPolicy.isChecked) {
//            signup()
//        } else {
//            mViewDataBinding.root.snackbar("Please Agree to continue")
//        }




        return true
    }
}