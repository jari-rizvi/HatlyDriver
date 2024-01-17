package com.teamx.hatlyDriver.ui.fragments.accountSetting

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentEditProfileBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.utils.PrefHelper
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_edit_profile
    override val viewModel: Class<EditProfileViewModel>
        get() = EditProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    var userName = ""
    var imageUrl = ""


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



        sharedViewModel.userData.observe(requireActivity()) {
            mViewDataBinding.etName.setText(it.name)
            mViewDataBinding.etPhone.setText(it.contact)
            Log.d("PersonalI", "onViewCreated: ${it.contact}")
//            if (it.contact == null) {
//                mViewDataBinding.editText2.isEnabled = true
//            } else {
//                mViewDataBinding.editText2.isEnabled = false
//                mViewDataBinding.editText2.setText(it.contact)
//            }

            Picasso.get().load(it.profileImage).placeholder(R.drawable.logo).error(R.drawable.logo).resize(500, 500).into(mViewDataBinding.hatlyIcon)
            imageUrl = it.profileImage
            userName = it.name
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


        mViewDataBinding.imgBack.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment, arguments, options)        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event here
                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment, arguments, options)


                }
            })

        mViewDataBinding.etPhone.isEnabled = false
        mViewDataBinding.etPhone.isFocusable = false
        mViewDataBinding.etPhone.isFocusableInTouchMode = false

       /*   val userData = PrefHelper.getInstance(requireActivity()).getUserData()

          mViewDataBinding.etName.setText(userData?.name)
          mViewDataBinding.etPhone.setText(userData?.contact)
          Picasso.get().load(userData?.profileImage).resize(500, 500).into(mViewDataBinding.hatlyIcon)*/


   /*     mViewModel.me()
        if (!mViewModel.meResponse.hasActiveObservers()) {
            mViewModel.meResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }  Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            imageUrl = data.profileImage
                            mViewDataBinding.etName.setText(data.name)
                            mViewDataBinding.etPhone.setText(data.contact)
                            Picasso.get().load(data.profileImage).resize(500, 500)
                                .into(mViewDataBinding.hatlyIcon)


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        mViewDataBinding.root.snackbar(it.message!!)
                    }
                }
            }
        }*/



        mViewDataBinding.btnChangePass.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.changePasswordFragment, arguments, options)
        }



        mViewDataBinding.btnSave.setOnClickListener {
            userName = mViewDataBinding.etName.text.toString()

                val params = JsonObject()
                try {
                    params.addProperty("name", userName)
                    params.addProperty("profileImage", imageUrl)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                mViewModel.updateProfile(params)
            }



        mViewDataBinding.imgGallery.setOnClickListener {
            fetchImageFromGallery()
        }


        if (!mViewModel.uploadReviewImgResponse.hasActiveObservers()) {
            mViewModel.uploadReviewImgResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (data.isNotEmpty()) {
                                if (data.isNotEmpty()) {
                                    imageUrl = data[0]
                                    Picasso.get().load(imageUrl).placeholder(R.drawable.logo).error(R.drawable.logo).resize(500, 500)
                                        .into(mViewDataBinding.hatlyIcon)
                                }
                            }
                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()

                        if (isAdded) {

                            mViewDataBinding.root.snackbar(it.message!!)
                        }
                    }
                }
            }
        }




        if (!mViewModel.updateProfileResponse.hasActiveObservers()) {
            mViewModel.updateProfileResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }  Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            Picasso.get().load(data.profileImage).resize(500, 500)
                                .into(mViewDataBinding.hatlyIcon)
                            mViewDataBinding.etName.setText(data.name)
                            val userData = PrefHelper.getInstance(requireActivity()).getUserData()
                            userData!!.name = data.name
                            userData!!.profileImage = data.profileImage
                            PrefHelper.getInstance(requireActivity()).setUserData(userData)
                            PrefHelper.getInstance(requireActivity()).setUserData(userData)
                            sharedViewModel.setUserData(userData)
                            if (isAdded) {
                                mViewDataBinding.root.snackbar("Profile updated")
                            }


                            val bundle = arguments
                            if (bundle != null) {
                                bundle.putString("userimg", data.profileImage)
                                bundle.putString("username", data.name)
                            }


                            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            navController.navigate(R.id.homeFragment, arguments, options)


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


    private fun fetchImageFromGallery() {
        startForResult.launch("image/*")
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val str = "${requireContext().filesDir}/file.jpg"

//                uploadWithRetrofit(it)
                val imageUri = uri

                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    imageUri
                )

// Compress the bitmap to a JPEG format with 80% quality and save it to a file
                val outputStream = FileOutputStream(str)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                outputStream.close()

//                Picasso.get().load(it).into(mViewDataBinding.hatlyIcon)

                uploadWithRetrofit(File(str))
            }
        }

    private fun uploadWithRetrofit(file: File) {

        val imagesList = mutableListOf<MultipartBody.Part>()

        imagesList.add(prepareFilePart("images", file))

        mViewModel.uploadReviewImg(imagesList)

    }

    private fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), fileUri)
        return MultipartBody.Part.createFormData(partName, fileUri.name, requestFile)
    }


}