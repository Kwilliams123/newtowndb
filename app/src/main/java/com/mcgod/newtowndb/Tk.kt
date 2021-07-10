package com.mcgod.newtowndb

import com.google.android.gms.common.util.DataUtils
import java.lang.Math.*
import java.text.SimpleDateFormat
import kotlin.math.abs

fun main(){


 var list = ArrayList<Members>()

    list.add(Members(bday="9/23/1990"))
    list.add(Members(bday="10/14/1993"))
    list.add(Members(bday="12/12/1981"))
    list.add(Members(bday="10/13/1979"))
    list.add(Members(bday="1/23/1993"))
    list.add(Members(bday="8/30/2000"))
    list.add(Members(bday="7/14/1978"))
    list.add(Members(bday="6/30/2004"))


    //print(list.rangeAge(30, 60))
    //print(DateUtils("9/23/1990").age())


    println("04/22/2021".numOfDays())
}



fun ArrayList<Members>.contains(constraint : String) : String{



    when(constraint){

        "age"->{




        }
       "occ", "occupation"->{


        }

        "bap","baptized" ->{


        }

        "dob"->{


        }

        "gen"->{


        }


    }

    return "hello"

}


fun String.f() = (this.subSequence(0, this.indexOf(",")) as String).trim()


fun String.s() = (this.substring(this.indexOf(",")+1, lastIndex+1).trim())


fun String.firstSecond() =

    when {

        contains(",") -> listOf(this.f(), this.s())
        else -> listOf("")
    }



fun ArrayList<Members>.equalsAge(age : Int): List<Any>{
    return this.filter { DateUtils(it.bday).age() == age }

}

fun ArrayList<Members>.greaterAge(age : Int): List<Any>{
    return this.filter { DateUtils(it.bday).age() > age }

}

fun ArrayList<Members>.lesserAge(age : Int): List<Any>{
    return this.filter { DateUtils(it.bday).age() < age }

}


fun ArrayList<Members>.rangeAge(lower : Int, upper: Int): List<Any>{

    return this.filter { DateUtils(it.bday).age() >= lower &&  DateUtils(it.bday).age() <= upper }

}



fun String.numOfDays(): Int{

   var d1 = DateUtils(this).countDay()
    return when{

        d1 >= 0 -> d1
        else -> 365 + d1
    }



}

















