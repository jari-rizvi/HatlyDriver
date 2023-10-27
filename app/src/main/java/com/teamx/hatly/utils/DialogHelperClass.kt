package com.teamx.hatly.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.teamx.hatly.R

class DialogHelperClass {
    companion object {

        fun loadingDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_loading)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            return dialog
        }


        fun errorDialog(context: Context, errorMessage: String) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_error)
            val errorTextMessage = dialog.findViewById<TextView>(R.id.tv_error_message)
            val tv_title_text = dialog.findViewById<TextView>(R.id.tv_title_text)
            tv_title_text.visibility = View.GONE
            if (errorMessage.contains("job was cancelled", true)) {
                return
            }
            errorTextMessage.text = errorMessage
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }



        interface ChangePasswordDialog {
            fun onLoginButton()
        }


        fun changePasswordDialog(context: Context, dialogCallBack: ChangePasswordDialog, boo: Boolean) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.password_change_dialog)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
            )


            val removeBtn = dialog.findViewById<TextView>(R.id.btnLogin)
            removeBtn.setOnClickListener {
                if (boo) {
                    dialogCallBack.onLoginButton()
                }

                val cancelBtn = dialog.findViewById<TextView>(R.id.imageView19)
                cancelBtn.setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        interface ConfirmLocationDialog {
            fun onConfirmLocation()
        }

        fun confirmLocation(context: Context, dialogCallBack: ConfirmLocationDialog, boo: Boolean) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_allow_location)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
            )

            val btnallow = dialog.findViewById<TextView>(R.id.btnAllowLocation)
            btnallow.setOnClickListener {
                dialogCallBack.onConfirmLocation()
                dialog.dismiss()
            }


            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }



        interface ReasonDialog {
            fun onSubmitClick(status:String, rejectionReason:String)
            fun onCancelClick()
        }


        fun submitReason(
            context: Context, reasonDialog: ReasonDialog, boo: Boolean, status: String,
            rejectionReason: String
        ) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_reason)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
            )

            val tvTitleText = dialog.findViewById<TextView>(R.id.tvLocation1)


            val AddReviewBtn = dialog.findViewById<TextView>(R.id.btnSubmit)

//            AddReviewBtn.text = context.getString(R.string.add_review)
            AddReviewBtn.setOnClickListener {
                val desc = tvTitleText.text
                if (desc.isBlank()) {
                    Snackbar.make(
                        AddReviewBtn,
                        "Please enter some text",
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener
                }
                if (boo) {
                    reasonDialog.onSubmitClick(status, desc.toString())
                } else {
                    reasonDialog.onCancelClick()
                }
                dialog.dismiss()
            }
            val cancelBtn = dialog.findViewById<ImageView>(R.id.imageView23)

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }



    }
}