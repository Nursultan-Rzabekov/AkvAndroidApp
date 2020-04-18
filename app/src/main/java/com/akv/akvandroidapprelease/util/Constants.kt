package com.akv.akvandroidapprelease.util


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

        val countryList = listOf("Казахстан")

        val regionList = listOf(
            ReqionList("Западно-Казахстанская область", 1),
            ReqionList("Мангистауская Область", 2),
            ReqionList("Атырауская область", 3),
            ReqionList("Актюбинская область", 4),
            ReqionList("Восточно-Казахстанская область", 5),
            ReqionList( "Акмолинская область", 6),
            ReqionList( "Северо-Казахстанская область", 7),
            ReqionList( "Павлодарская Область", 8),
            ReqionList("Кызылординская Область", 9),
            ReqionList("Костанайская область", 10),
            ReqionList("Карагандинская область", 11),
            ReqionList("Жамбылская область", 12),
            ReqionList("Туркестанская область", 13),
            ReqionList("Город Алматы", 14),
            ReqionList("Алматинская Область", 15),
            ReqionList("Шымкент", 16),
            ReqionList("Город Байконыр", 17),
            ReqionList("Нур-Султан", 18)
        )

        val cityList = listOf(
            CityList(1,"Жанаозен", 2),
            CityList(2,"Шалкар", 4),
            CityList(3,"Шалкар", 3),
            CityList(4,"Уральск", 1),
            CityList(5,"Кандыагаш", 4),
            CityList(6,"Кульсары", 3),
            CityList(7,"Хромтау", 4),
            CityList(8,"Караганда", 11),
            CityList(9,"Эмба", 4),
            CityList(10,"Балыкши", 3),
            CityList(11,"Атырау", 3),
            CityList(12,"Актюбинск",4),
            CityList(13,"Актау",2),
            CityList(14,"Аксай",1),
            CityList(15,"Зырьяновск",5),
            CityList(16,"Джусалы",9),
            CityList(17,"Жезқазған",11),
            CityList(18,"Джетыгара",10),
            CityList(19,"Джетыгара",12),
            CityList(20,"Айтеке-Би",9),
            CityList(21,"Тараз",12),
            CityList(22,"Зайсан",5),
            CityList(23,"Яныкурган",9),
            CityList(24,"Турара Рыскулова",13),
            CityList(25,"Уштобе",15),
            CityList(26,"Туркестан",13),
            CityList(27,"Темиртау",11),
            CityList(28,"Текели",15),
            CityList(29,"Тасбогет",9),
            CityList(30,"Талгар",15),
            CityList(31,"Талдыкорган",15),
            CityList(32,"Шымкент",16),
            CityList(33,"Шу",12),
            CityList(34,"Шемонаиха",5),
            CityList(35,"Щучинск",6),
            CityList(36,"Шахтинск",11),
            CityList(37,"Семей",5),
            CityList(38,"Сарыагаш",13),
            CityList(39,"Сарканд",14),
            CityList(40,"Сарань",11),
            CityList(41,"Рудный",10),
            CityList(42,"Кызылорда",9),
            CityList(43,"Кустанай",10),
            CityList(44,"Каратау",12),
            CityList(45,"Капшагай",15),
            CityList(46,"Петропавловск",7),
            CityList(47,"Павлодар",8),
            CityList(48,"Панфилов",15),
            CityList(49,"Усть-Каменогорск",5),
            CityList(50,"Сарыкемер",12),
            CityList(51,"Мерке",12),
            CityList(52,"Макинск",6),
            CityList(53,"Лисаковск",10),
            CityList(54,"Байконур",17),
            CityList(55,"Риддер",5),
            CityList(56,"Ленгер",13),
            CityList(57,"Кокшетау",6),
            CityList(58,"Кентау",13),
            CityList(59,"Иссык",15),
            CityList(60,"Георгиевка",5),
            CityList(61,"Аксу",8),
            CityList(62,"Энергетический",15),
            CityList(63,"Экибастуз",8),
            CityList(64,"Шиели",9),
            CityList(65,"Шардара",13),
            CityList(66,"Бурундай",15),
            CityList(67,"Белые Воды",13),
            CityList(68,"Балхаш",11),
            CityList(69,"Аягоз",5),
            CityList(70,"Атбасар",6),
            CityList(71,"Арысь",13),
            CityList(72,"Аркалык",10),
            CityList(73,"Аральск",9),
            CityList(74,"Нур-Султан",18),
            CityList(75,"Алматы",14),
            CityList(76,"Акколь’",6),
            CityList(77,"Абай",11),
            CityList(78,"Степногорск",6)
        )

        val typeList = listOf(
            ReqionList("Квартира", 1),
            ReqionList("Дом", 2)
        )
    }
}

data class CityList(val id :Int, val name:String, val region_id:Int)
data class ReqionList(val name:String, val id :Int)