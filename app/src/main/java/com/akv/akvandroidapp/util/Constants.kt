package com.akv.akvandroidapp.util


class Constants {

    companion object{
        const val BASE_URL = "https://akv.kz/api/"//"http://akv-technopark.herokuapp.com/api/"//
        const val BASE_URL_IMAGE = "https://akv.kz"// "http://akv-technopark.herokuapp.com"//"
        const val PASSWORD_RESET_URL: String = "https://open-api.xyz/password_reset/"
        const val PAYBOX_PAY_URL: String = "https://open-api.xyz/password_reset/"
        const val AKV_TAX = 5
        const val AKV_PHONE = "+77026513909"

        const val SUCCESS = "Success"

        const val NOT_VALID_IMAGE = "http://akv-technopark.herokuapp.com/media/images/no.jpg"

        const val DEFAULT_YEAR_GAP = -12

        const val ALL_PERMISSIONS_RESULT = 101
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong()

        const val FILTER_MAX_PRICE = 1000000
        const val FILTER_MAX_PRICE_INTERVAL = 10000
        const val FILTER_MAX_ROOMS = 15
        const val FILTER_MAX_BEDS = 30

        const val RESERVATION_REQUEST = 0
        const val RESERVATION_APPROVED = 1
        const val RESERVATION_REJECTED = 2
        const val RESERVATION_PAID = 3
        const val RESERVATION_CANCELED = 4
        const val RESERVATION_EXPIRED = 5

        const val NETWORK_TIMEOUT = 60000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val PAGINATION_PAGE_SIZE = 10
        const val PAGINATION_PAGE_SIZE_FAVORITE = 10
        const val PAGINATION_MESSAGE_SIZE = 15

        const val GALLERY_REQUEST_CODE = 0
        const val REQUEST_IMAGE_CAPTURE = 15
        const val PICK_FILE_CODE = 30

        const val FONT_SIZE = 15f
        const val MARGIN_SIZE = 3f
        const val STROKE_SIZE = 3f

        const val GALLERY_REQUEST_CODE_SAVE = 501
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val CROP_IMAGE_INTENT_CODE: Int = 401

        const val MAPKIT_API_KEY = "73777cb6-74ce-462b-874c-21db3b1bc84f"

        const val FILTER_TYPE1 = "Нет"
        const val FILTER_TYPE2 = "rating"
        const val FILTER_TYPE3 = "price"

        const val MESSAGE_TYPE_TEXT = 0
        const val MESSAGE_TYPE_PHOTO = 1
        const val MESSAGE_TYPE_DOC = 2
        const val MESSAGE_TYPE_REQUEST = 3

        const val TOTAL_MESSAGES_COUNT = 100

        val mapFacilities = mapOf(
            "Отопление" to 1,
            "Wifi" to 2,
            "Кондиционер" to 3,
            "Аптечка" to 4,
            "Чайник" to 5,
            "Фен" to 6,
            "Утюг" to 7,
            "Полотенца" to 8,
            "Телевизор" to 9
        )
        val mapFacilitiesReversed = mapFacilities.entries.associateBy({ it.value }) { it.key }

        val mapTypes = mapOf(
            "Любой" to 0,
            "Квартира" to 1,
            "Дом" to 2
        )

        val mapTypesReversed = mapTypes.entries.associateBy({ it.value }) { it.key }

        val mapCity = mapOf(
            "Астана" to 1,
            "Алматы" to 2,
            "Шымкент" to 3,
            "Семей" to 4,
            "Павлодар" to 5,
            "Тараз" to 6,
            "Актобе" to 7
        )

        val mapTypeHouse = mapOf(
            "Квартира" to 1,
            "Дом" to 2,
            "Коттедж" to 3,
            "Комната" to 4
        )

        val mapGeolocation = mapOf(
            "Никогда" to 0,
            "При использовании" to 1,
            "Всегда" to 2
        )

        val mapGeolocationReversed = mapGeolocation.entries.associateBy({ it.value }) { it.key }

        val countryList = listOf("Kazakhstan")

        val regionList = listOf(
            ReqionList("Batys Qazaqstan", 1),
            ReqionList("Mangghystaū", 2),
            ReqionList("Atyraū", 3),
            ReqionList("Aqtöbe", 4),
            ReqionList("East Kazakhstan", 5),
            ReqionList( "Aqmola", 6),
            ReqionList( "Soltüstik Qazaqstan", 7),
            ReqionList( "Pavlodar Region", 8),
            ReqionList("Qyzylorda", 9),
            ReqionList("Qostanay", 10),
            ReqionList("Karaganda", 11),
            ReqionList("Zhambyl", 12),
            ReqionList("South Kazakhstan", 13),
            ReqionList("Almaty", 14),
            ReqionList("Almaty Oblysy", 15),
            ReqionList("Shymkent", 16),
            ReqionList("Baikonur", 17),
            ReqionList("Nur-Sultan", 18)
        )

        val cityList = listOf(
            CityList(1,"Zhanaozen", 2),
            CityList(2,"Shalqar", 4),
            CityList(3,"Shalkar", 3),
            CityList(4,"Oral", 1),
            CityList(5,"Kandyagash", 4),
            CityList(6,"Qulsary", 3),
            CityList(7,"Khromtau", 4),
            CityList(8,"Karagandy", 11),
            CityList(9,"Embi", 4),
            CityList(10,"Balykshi", 3),
            CityList(11,"Atyrau", 3),
            CityList(12,"Aktobe",4),
            CityList(13,"Aktau",2),
            CityList(14,"Aqsay",1),
            CityList(15,"Zyryanovsk",5),
            CityList(16,"Zhosaly",9),
            CityList(17,"Zhezqazghan",11),
            CityList(18,"Dzhetygara",10),
            CityList(19,"Zhangatas",12),
            CityList(20,"Ayteke Bi",9),
            CityList(21,"Taraz",12),
            CityList(22,"Zaysan",5),
            CityList(23,"Yanykurgan",9),
            CityList(24,"Turar Ryskulov",13),
            CityList(25,"Ush-Tyube",15),
            CityList(26,"Turkestan",13),
            CityList(27,"Temirtau",11),
            CityList(28,"Tekeli",15),
            CityList(29,"Tasböget",9),
            CityList(30,"Talghar",15),
            CityList(31,"Taldykorgan",15),
            CityList(32,"Shymkent",16),
            CityList(33,"Chu",12),
            CityList(34,"Shemonaīkha",5),
            CityList(35,"Shchuchinsk",6),
            CityList(36,"Shakhtinsk",11),
            CityList(37,"Semey",5),
            CityList(38,"Saryaghash",13),
            CityList(39,"Sarkand",14),
            CityList(40,"Soran",11),
            CityList(41,"Rudnyy",10),
            CityList(42,"Kyzylorda",9),
            CityList(43,"Kostanay",10),
            CityList(44,"Karatau",12),
            CityList(45,"Kapshagay",15),
            CityList(46,"Petropavl",7),
            CityList(47,"Pavlodar",8),
            CityList(48,"Zharkent",15),
            CityList(49,"Ust-Kamenogorsk",5),
            CityList(50,"Sarykemer",12),
            CityList(51,"Merke",12),
            CityList(52,"Makinsk",6),
            CityList(53,"Lisakovsk",10),
            CityList(54,"Baikonur",17),
            CityList(55,"Ridder",5),
            CityList(56,"Ridder",13),
            CityList(57,"Kokshetau",6),
            CityList(58,"Kentau",13),
            CityList(59,"Esik",15),
            CityList(60,"Georgīevka",5),
            CityList(61,"Aksu",8),
            CityList(62,"Otegen Batyra",15),
            CityList(63,"Ekibastuz",8),
            CityList(64,"Shīeli",9),
            CityList(65,"Chardara",13),
            CityList(66,"Burunday",15),
            CityList(67,"Belyye Vody",13),
            CityList(68,"Balqash",11),
            CityList(69,"Ayagoz",5),
            CityList(70,"Atbasar",6),
            CityList(71,"Arys",13),
            CityList(72,"Arkalyk",10),
            CityList(73,"Aral",9),
            CityList(74,"Nur-Sultan",18),
            CityList(75,"Almaty",14),
            CityList(76,"Akkol’",6),
            CityList(77,"Abay",11),
            CityList(78,"Stepnogorsk",6)
        )

        val typeList = listOf(
            ReqionList("Квартира", 1),
            ReqionList("Дом", 2)
        )
    }
}

data class CityList(val id :Int, val name:String, val region_id:Int)
data class ReqionList(val name:String, val id :Int)