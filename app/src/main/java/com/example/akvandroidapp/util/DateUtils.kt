package com.example.akvandroidapp.util

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
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
                return Date()
            }
        }

        fun convertLongStringToDate(date: String): Date{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.ENGLISH)
            var d: Date
            try {
                d = sdf.parse(date)?:Date()
            } catch (e: Exception){
                Log.d("DateUtils:Convert", "string to date convertion: $e")
                d = Date()
            }
            return d
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

        fun convertDateToStringForBooking(date: Date): String{
            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
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

        fun getDatesBetween(d1: Date, d2: Date): List<Date> {
            val dates = arrayListOf<Date>()
            val startcal = Calendar.getInstance()
            startcal.time = d1
            val endcal = Calendar.getInstance()
            endcal.time = d2

            while (startcal.before(endcal)){
                dates.add(startcal.time)
                startcal.add(Calendar.DATE, 1)
            }

            return dates
        }

        fun getDateFromNYear(n: Int): Date{
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, n)

            return c.time
        }

        fun getDatesFromToday(dates: List<Date>): List<Date> {
            val today = Calendar.getInstance()
            val fromToday = mutableListOf<Date>()
            for (date in dates){
                if (today.time <= date)
                    fromToday.add(date)
            }
            return fromToday
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

    @Parcelize
    data class DateBundle(
        val date: Date
    ): Parcelable
}