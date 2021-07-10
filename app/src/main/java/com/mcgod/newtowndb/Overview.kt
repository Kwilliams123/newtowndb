package com.mcgod.newtowndb

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.overview.*
import kotlinx.android.synthetic.main.overview.contact
import kotlinx.android.synthetic.main.overview.name
import kotlinx.android.synthetic.main.personal.*

class Overview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.overview)

       // val member : Members =

        members = intent.getSerializableExtra("members") as Members

        //System.out.println(members.name)

        //Toast.makeText(this@Overview, members.name, Toast.LENGTH_LONG).show()


        init()

    }

    companion object {

        lateinit var members : Members
    }
    fun init(){

        image.setImageResource(NameUtils(members.name).getAlphaPic()!!)
        name.text    = members.name
        contact.text = members.contact
        bday.text    = members.bday
        genders.text = members.gender.capitalize()
        occupations.text = members.occupation
        mstatuss.text = members.mstatus
        locations.text = members.location
        baptiz.text = when(members.baptized){

            "yes" -> "Baptized in water"
            else -> "Not baptized in water"
        }
        baptize_spirit.text = when(members.sbaptized){

            "yes" -> "Speaks in tongues"
            else -> "Does not speak in tongues"
        }

    }
}