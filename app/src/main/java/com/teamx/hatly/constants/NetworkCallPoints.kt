package com.teamx.hatly.constants

class NetworkCallPoints {
    companion object {
        const val LOGIN_PHONE = "auth/login/app";
        const val SIGN_UP = "driver";

        // Register Otp Verify

        const val OTP_VERIFY = "register/phone-verify";
        const val OTP_VERIFY_PHONE = "verify-phone-otp-code";
        const val RESEND_OTP_VERIFY = "register/resend-otp";


        // Create new password
        const val RESET_PASS = "reset-password";
        const val RESET_PASS_PHONE = "Reset-Password-Phone";
        const val CHANGE_PASSWORD = "change-password";


        const val FORGOT_PASS_PHONE = "forget-password-phone";



        const val GET_ORDERS = "incoming-requests";
        const val GET_ORDERS_BYSTATUS = "incoming-requests";


        
        var TOKENER = ""
    }
}