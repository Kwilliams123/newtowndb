package com.mcgod.newtowndb

import java.io.Serializable

data class Members(   val id :       String = "0",
                      val name:      String = "",
                      val contact:   String = "",
                      val bday:      String = "",
                      val location:  String = "",
                      val gender  :  String = "",
                      val mstatus:   String = "",
                      val occupation:String = "",
                      val baptized:  String = "",
                      val sbaptized: String = "",
                      val pic:       String = "") : Serializable


