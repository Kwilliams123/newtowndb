package com.mcgod.newtowndb

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBOffline(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {

        p0?.execSQL(""" CREATE TABLE $TABLE_NAME ( INTEGER $ID , 
            | VARCHAR(100) $NAME, 
            | VARCHAR(30)  $CONTACT , 
            | VARCHAR(20)  $BDAY,
            | VARCHAR(100) $LOCATION, 
            | VARCHAR(20)  $GENDER,
            | BLOB         $PIC ,
            | VARCHAR(100) $OCCUPATION, 
            | VARCHAR(30)  $BAPTIZED,
            | VARCHAR(30)  $S_BAPTIZED, 
            | VARCHAR(30)  $MSTATUS )
     
            
        """.trimMargin())

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)

    }

    companion object {

        const val VERSION     = 1
        const val DB_NAME     = "Newtown.db"
        const val TABLE_NAME  = "newtown"
        const val ID          = "id"
        const val NAME        = "name"
        const val CONTACT     = "contact"
        const val BDAY        = "dob"
        const val LOCATION    = "location"
        const val MSTATUS     = "mstatus"
        const val GENDER      = "gender"
        const val OCCUPATION  = "occupation"
        const val PIC         = "pic"
        const val GPS         = "gps"
        const val BAPTIZED    = "baptized"
        const val S_BAPTIZED  = "spirit_baptized"

    }


    fun getData() : ArrayList<MutableMap<String, Any>> {
       val data =  ArrayList<MutableMap<String, Any>>()
       val cursor: Cursor  = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $BDAY DESC", null)

        cursor.let {

            do {

              val map : MutableMap<String, Any> = HashMap()

                map[ID]       = it.getColumnName(it.getColumnIndex(ID))
                map[NAME]     = it.getColumnName(it.getColumnIndex(NAME))
                map[BDAY]     = it.getColumnName(it.getColumnIndex(BDAY))
                map[CONTACT]  = it.getColumnName(it.getColumnIndex(CONTACT))
                map[LOCATION] = it.getColumnName(it.getColumnIndex(LOCATION))
                map[GENDER]   = it.getColumnName(it.getColumnIndex(GENDER))
                map[MSTATUS]  = it.getColumnName(it.getColumnIndex(MSTATUS))
                map[BAPTIZED] = it.getColumnName(it.getColumnIndex(S_BAPTIZED))
                map[PIC]      = it.getColumnName(it.getColumnIndex(PIC))

                data.add(map)

            }while (it.moveToNext())
        }

        return data
    }


    fun clearData(){

        val sd = readableDatabase
        sd.rawQuery("DELETE FROM $TABLE_NAME", null)
    }




    fun insertData(data : ArrayList<Members>){

        val ds = readableDatabase

        for( members in data) {

            ds.rawQuery(
                """INSERT INTO $TABLE_NAME (
                $ID,
                $NAME,
                $CONTACT,
                $BDAY,
                $LOCATION,
                $GENDER,
                $PIC ,
                $OCCUPATION,
                $BAPTIZED,
                $S_BAPTIZED,
                $MSTATUS ) 
                VALUES ('${members.id}',
                        '${members.name}',
                        '${members.contact}',
                        '${members.bday}',
                        '${members.location}',
                        '${members.gender}',
                        '${members.pic}',
                        '${members.occupation}',
                        '${members.baptized}',
                        '${members.sbaptized}',
                        '${members.mstatus}')""", null
            )

        }

    }

    fun isTableEmpty() : Boolean{

        val sd = readableDatabase
        val cursor =   sd.rawQuery("SELECT $ID FROM $TABLE_NAME LIMIT 1 ", null)
        return  cursor.isFirst



    }
}