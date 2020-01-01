package com.example.akvandroidapp.util

class Constants {

    companion object{

        const val BASE_URL = "http://akv-technopark.herokuapp.com/api/"
        const val PASSWORD_RESET_URL: String = "https://open-api.xyz/password_reset/"

        const val NETWORK_TIMEOUT = 60000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val PAGINATION_PAGE_SIZE = 3

        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val CROP_IMAGE_INTENT_CODE: Int = 401

        const val MAPKIT_API_KEY = "921f9910-6b90-4ad6-afc1-c4fd6898e3c0"

    }
}