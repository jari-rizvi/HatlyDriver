package com.teamx.hatly.constants

class NetworkCallPoints {
    companion object {
        const val LOGIN_PHONE = "token/phone";
        const val LOGIN = "token";
        const val SIGN_UP = "register/phone";
        const val HOME = "dashboard";
        const val NEARBY = "shops/nearby";
        const val NOTIFICATION = "notifications";
        const val GET_ALL_CARDS = "stripe/cards";
        const val COUPOUNS = "coupons";
        const val APPLY_COUPONS = "coupons/couponecode";

        // Register Otp Verify

        const val OTP_VERIFY = "register/phone-verify";
        const val OTP_VERIFY_PHONE = "verify-phone-otp-code";
        const val RESEND_OTP_VERIFY = "register/resend-otp";
        const val VERIFY_GOOGLE_SIGNIN = "register/google";
        const val VERIFY_FACEBOOK_SIGNIN = "register/facebook";


        // Create new password
        const val RESET_PASS = "reset-password";
        const val RESET_PASS_PHONE = "Reset-Password-Phone";
        const val CHANGE_PASSWORD = "change-password";


        const val FORGOT_PASS_PHONE = "forget-password-phone";


        const val SHOP_BY_SLUG = "shops/{slug}";
        const val PRODUCTS_BY_SHOP_ID = "products";
        const val PRODUCTS_BY_SLUG = "products/{slug}";


        const val OTP_VERIFY_FORGOT = "verify-forget-password-token";

        const val PROFILE_USER = "me"
        const val UPLOAD_ATTACH = "attachments/images"

        //Reviews
        const val GET_ALL_REVIEWS = "reviews"
        const val GET_ORDER_REVIEW = "review/getAllReviews"
//        const val GET_ALL_REVIEWS = "reviews?product={id}"

        //payment Strip
        const val DEL_ALL_CARDS = "stripe/cards/delete/{id}"

        const val DEL_FOURITE_SHOP = "shops/favourite/delete"
//        const val DEL_ALL_CARDS = "stripe/cards/delete"

        //Orders
        const val ORDER_LIST = "orders"

        const val ALL_FAVOURITE_SHOPS = "shops/getFavoriteShops"

        const val FAVOURITE_SHOP = "shops/favourite"

        const val GET_ORDER_BY_ID = "orders/{id}"

        const val UPDATE_ADDRESS_ID = "addresses/{id}"

        const val CREATE_ADDRESS = "addresses"

        const val CUSTOMER_ADDRESS = "addresses/customer"

        const val ADDRESS_ID = "addresses/{id}"

        const val VERIFY_CHECKOUT_ORDER_BY_ID = "orders/checkout/verify"


        const val CREATE_REVIEW = "review/create"


        var TOKENER = ""
    }
}