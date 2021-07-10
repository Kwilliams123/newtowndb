package com.mcgod.newtowndb
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.personal.*
import kotlinx.android.synthetic.main.registeration.*
import kotlinx.android.synthetic.main.take_image.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class Registration : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeration)
        first.on()
        second.next()
        third.next()


        db = DBOffline(this@Registration)

        titles.text = "Personal Information"

        mstatus.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                buttonId = checkedId
            })

        baptized.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                water = checkedId
            })

        baptized_spirit.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    spirit = checkedId
                })



       gender.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                gen = checkedId
            })




        val now = Calendar.getInstance()
         dpd = DatePickerDialog.newInstance(
             this@Registration,
             now[Calendar.YEAR],  // Initial year selection
             now[Calendar.MONTH],  // Initial month selection
             now[Calendar.DAY_OF_MONTH] // Initial day selection
         )

         file = File(cacheDir, "Newtown")

         file.mkdirs()

        if (!file.exists()) {

            //file.mkdirs()
            Toast.makeText(
                applicationContext, "Directory does not exist, create it",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun pickDate(view: View){

        dpd.show(supportFragmentManager, "Datepickerdialog")
    }




    fun nexts(view: View){

    when(inc % 3) {

        1 -> {


            if (name.text.isEmpty() || contact.text.isEmpty() || dob.text.isEmpty()) {
                Toast.makeText(this, "Make sure all the information is filled", Toast.LENGTH_LONG)
                    .show();
                return
            }


            information.put(DBOffline.NAME, name.text.toString())
            information.put(DBOffline.CONTACT, contact.text.toString())
            information[DBOffline.BDAY] = DateUtils(dob.text.toString(), "").unformat()

            first.next()
            second.on()
            third.next()

            personal.visibility = View.GONE
            social.visibility = View.VISIBLE

        }
        2 -> {

            if (occupation.text.isEmpty() || buttonId == 0 || location.text.isEmpty()) {

                Toast.makeText(this, "Make sure all the information is filled", Toast.LENGTH_LONG)
                    .show();
                return
            }

            information.put(DBOffline.OCCUPATION, occupation.text.toString())
            information.put(DBOffline.MSTATUS, status.get(statusCheck.indexOf(buttonId)))
            information.put(DBOffline.LOCATION, location.text.toString())


            social.visibility = View.GONE
            spiritual.visibility = View.VISIBLE
            first.next()
            second.next()
            third.on()
            next.setText("Finish")

        }
        0 -> {

            if (water == 0 || spirit == 0) {

                Toast.makeText(this, "Make sure all the information is filled", Toast.LENGTH_LONG)
                    .show()
                return
            }

            information.put(DBOffline.BAPTIZED, yesOrNo.get(baptizeCheck.indexOf(water)))
            information.put(DBOffline.S_BAPTIZED, yesOrNo.get(baptizeSpiritCheck.indexOf(spirit)))

            if (picDir.isEmpty()) information.put(DBOffline.PIC, "")

            var key = sFormate.format(Date())

            val rootRef = FirebaseDatabase.getInstance().reference




            val member =     Members(
            name       = name.text.toString(),
            contact    = contact.text.toString(),
            bday       = DateUtils(dob.text.toString(), "").unformat(),
            location   = location.text.toString(),
            gender     = genders.get(gend.indexOf(gen)),
            pic        = picDir,
            occupation = occupation.text.toString(),
            baptized   = yesOrNo.get(baptizeCheck.indexOf(water)),
            sbaptized  = yesOrNo.get(baptizeSpiritCheck.indexOf(spirit)),
            mstatus    = status.get(statusCheck.indexOf(buttonId)),
            id         = key
            )
            rootRef.child("newtown").child(key).setValue(member)
            //Log.d("TAg", members.toString())

            //db.insertData(member)




            startActivity(Intent(this@Registration, Home::class.java))
            finish()


        }

    }
        if(inc < 3)

        inc++
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

           // expandedImage.setImageURI(destinationUri)

            Picasso.
            with(this@Registration)

                .load(destinationUri)
                .into(expandedImage)

            picDir = destinationUri.toString()

            information.put(DBOffline.PIC, picDir)

        }else if(requestCode == IMAGE_PICK_CODE){

            var path = sFormate.format(Date())

            destinationUri = Uri.fromFile(File(file, path))
            //Toast.makeText(this@Registration , data!!.data!!.toString(), Toast.LENGTH_LONG).show()

            Log.d("TAG", data!!.data!!.toString())

            UCrop.of(data!!.data!!, destinationUri)
                .start(this@Registration)

        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
        }
    }

    fun View.next(){
        this.setBackgroundResource(R.drawable.pager_round)
        val params = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.next_height),
            resources.getDimensionPixelSize(R.dimen.next_height)
        )
        this.layoutParams = params
    }

    fun View.on(){

        this.setBackgroundResource(R.drawable.pager_checked)
        val params = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.on_height),
            resources.getDimensionPixelSize(R.dimen.on_height)
        )
        this.layoutParams = params
    }


    fun takePicture(view: View){

        var alert : AlertDialog.Builder = AlertDialog.Builder(this@Registration)

        var view = LayoutInflater.from(this@Registration).inflate(R.layout.take_image, null)

        alert.setView(view);

        var gallery : LinearLayout = view.gallery_parent

        gallery.setOnClickListener{

            pickImageFromGallery()
        }
        dialog  = alert.create()
        dialog.show()
    }


    companion object{

        var inc = 1
        lateinit var dpd: DatePickerDialog
        var information : MutableMap<String, Any> = hashMapOf()
        lateinit var sourceUri : Uri
        lateinit var destinationUri : Uri
        var buttonId = 0
        var spirit   = 0
        var water    = 0
        var gen      = 0

        lateinit var dialog : Dialog
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
        lateinit var file: File
        var sFormate                = SimpleDateFormat("yyyy_dd_MM_hh:mm:ss")
        var status                  = listOf("Married", "Single", "Divorced", "Widow(er)")
        var statusCheck             = listOf(R.id.m, R.id.s, R.id.d, R.id.w)
        var baptizeSpiritCheck      = listOf(R.id.baptized_spirit_yes, R.id.baptized_spirit_no)
        var  baptizeCheck           = listOf(R.id.baptized_yes, R.id.baptized_no)
        var genders                 = listOf("Male", "Female")
        var gend                    = listOf(R.id.male, R.id.female)
        var yesOrNo                 = listOf("yes", "no")
        var picDir                  = ""

        lateinit var db: DBOffline

    }

    private fun pickImageFromGallery() {
        //Intent to pick image

        dialog.dismiss()
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun getKey(map: MutableMap<String, Any>): String{

        var key = map.get(DBOffline.NAME).toString().random()
        return key.toString()
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        dob.setText("$dayOfMonth/${monthOfYear + 1}/$year")
    }

    //handle result of picked image
}

