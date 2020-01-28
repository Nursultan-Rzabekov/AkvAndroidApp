package com.example.akvandroidapp.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object{

        private val TAG: String = "AppDebug"

        // dates from server look like this: "2019-07-23T03:28:01.406944Z"
        fun convertServerStringDateToLong(sd: String): Long{
            var stringDate = sd.removeRange(sd.indexOf("T") until sd.length)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val time = sdf.parse(stringDate).time
                return time
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        fun convertLongToStringDate(longDate: Long): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val date = sdf.format(Date(longDate))
                return date
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        fun convertStringToDate(date: String): Date{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val d = sdf.parse(date)
                return d
            } catch (e: Exception){
                throw Exception(e)
            }
        }

        fun convertDateToString(date: Date): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val d = sdf.format(date)
                return d
            }catch (e: Exception){
                throw Exception(e)
            }
        }

        fun convertDateToStringWithSlash(date: Date): String{
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val d = sdf.format(date)
                return d
            }catch (e: Exception){
                throw Exception(e)
            }
        }

        fun convertCalendarToDate(year: Int, month: Int, day: Int): Date{
            val c = GregorianCalendar(year, month, day)
            return c.time
        }

        fun getDateFromNYear(n: Int): Date{
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, n)

            return c.time
        }

        fun getOtherDates(dates: List<Date>, start: Date, end: Date): ArrayList<Date>{
            val others = arrayListOf<Date>()
            val startcal = Calendar.getInstance()
            startcal.time = start
            val endcal = Calendar.getInstance()
            endcal.time = end

            val dates_reserved = arrayListOf<Calendar>()
            for (date in dates)
                dates_reserved.add(
                    Calendar.getInstance().apply {
                        time = date
                    }
                )

            while (startcal.before(endcal)){
                if (!dates_reserved.contains(startcal))
                    others.add(
                        startcal.time
                    )
                startcal.add(Calendar.DATE, 1)
            }

            return others
        }
    }


}