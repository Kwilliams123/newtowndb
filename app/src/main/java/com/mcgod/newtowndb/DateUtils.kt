package com.mcgod.newtowndb

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateUtils{

    constructor(dates:String){
        try {
            date = unform.parse(dates)

        }catch (ex: Exception){
        }

    }

    constructor(dates: String, des: String){
        try {

            date = format.parse(dates)
        }catch (ex: Exception){

        }
    }

    fun getDaySpecial() = "${getWeekNumber()}${getWeekNumber().dayPosition()}"

    fun getDay()  =
        when(day.format(date).length){

        1 -> "0${day.format(date)}"
        else -> day.format(date)
    }

    fun getRawDay() = day.format(date)
    fun getYear() = year.format(date)
    fun getWeek() = when(getMonth()) {

        month.format(Date()) -> week.format(date)
        else -> monthName.format(date)
    }
    fun getDate(): String = format.format(date)

    fun unformat() :String = unform.format(date)


    fun getDayMonth(): String = dayMonth.format(date)

    fun getMonth(): String = month.format(date)
    fun getWeekNumber() : Int{

        cal.set(cal.get(Calendar.YEAR), getMonth().toInt() + 1, getDay().toInt())

        return cal.get(Calendar.WEEK_OF_MONTH)
    }


    companion object{
        var day = SimpleDateFormat("d")
        var month = SimpleDateFormat("M")
        var monthName = SimpleDateFormat("MMM")
        var fullMonth = SimpleDateFormat("MMMM")
        var dayMonthSpecial = SimpleDateFormat("MMM, d")
        var year = SimpleDateFormat("yyyy")
        var week = SimpleDateFormat("EE")
        var unform = SimpleDateFormat("MM/dd/yyyy")
        var format = SimpleDateFormat("dd/MM/yyyy")
        var dayMonth = SimpleDateFormat("dd/MM")
        lateinit var date : Date
        var cal = Calendar.getInstance()
    }

    fun specialDayMonth(): String = dayMonthSpecial.format(date)

    fun countDays(): String = "${getDay().toInt() - day.format(Date()).toInt()}d"
    fun countDay(): Int = getDay().toInt() - day.format(Date()).toInt()
    fun getMonthYear() : String = "${fullMonth.format(date)}, ${year.format(Date())}"


    fun Int.dayPosition() = when(this){
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }

    fun specialWeek() = week.format(date)



    fun belated(): Int{
        // today ->    09/01/2021 -> 0
        // past ->    05/01/2021  -> -1
        // future -> 12/02/2021   -> 1
     return   when(getMonth()!!.toInt() > month.format(Date()).toInt()){

            true -> 1 // future
            false -> when(getMonth()!! == month.format(Date())){

                false -> -1 // past
                true -> when(getDay().toInt() ==  day.format(Date()).toInt()){

                    true ->   0 // today
                    false -> when(getDay().toInt() > day.format(Date()).toInt()){

                        true -> 2 // present
                        false -> -1 // past
                    }
                }
            }


        }


    }


    fun age() = when{

        (getMonth().toInt() <= month.format(Date() ).toInt() ) -> year.format(Date()).toInt() - getYear().toInt()

        else -> year.format(Date()).toInt() - getYear().toInt() - 1
        }




    }







