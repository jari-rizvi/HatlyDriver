package com.teamx.hatly.constants

import androidx.annotation.StringDef


object AppConstants {

    @StringDef(ApiConfiguration.BASE_URL)
    annotation class ApiConfiguration {
        companion object {
            //            const val BASE_URL = "https://raseefapi.teamxmv.com/api/"
            const val BASE_URL = "https://api.raseef.app/api/"
//            const val BASE_URL = "http://151.106.113.197:5001/api/"
//            const val BASE_URL = "http://192.168.18.22:5002/api/"
//            const val BASE_URL = "http://192.168.100.22:5002/api/"

            //            const val BASE_URL = "https://api.teamxmv.com/api/"
//            const val BASE_URL2 = "https://zues.teamxmv.com/api/"
        }
    }

    @StringDef(DbConfiguration.DB_NAME)
    annotation class DbConfiguration {
        companion object {
            const val DB_NAME = "RaseefProject"
        }
    }


    @StringDef(
        DataStore.DATA_STORE_NAME,
        DataStore.LOCALIZATION_KEY_NAME,
        DataStore.USER_NAME_KEY,
        DataStore.TOKEN,
        DataStore.DETAILS,
        DataStore.PAYMENT,
        DataStore.SAVE_ID,
        DataStore.AVATAR,
        DataStore.USER_ID,
        DataStore.NUMBER,
        DataStore.CARPLATE,
        DataStore.NAME,
        DataStore.PAYTYPE,
        DataStore.EMAIL
    )

    annotation class DataStore {
        companion object {
            const val DATA_STORE_NAME = "BaseProject"
            const val LOCALIZATION_KEY_NAME = "lang"
            const val USER_NAME_KEY = "user_name_key"
            const val TOKEN = "token"
            const val DETAILS = "details"
            const val USER_ID = "user_id"
            const val PAYMENT = "payment"
            const val SAVE_ID = "save_id"
            const val AVATAR = "avatar"
            const val NUMBER = "number"
            const val CARPLATE = "car_plate"
            const val PAYTYPE = "pay_type"
            const val NAME = "name"
            const val EMAIL = "email"
        }
    }

}