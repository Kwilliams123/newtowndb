package com.mcgod.newtowndb

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.one_row_home.view.*
import kotlinx.android.synthetic.main.personal.*
import kotlinx.android.synthetic.main.search.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Home :AppCompatActivity(), OnClickMember {

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        initRV()
        initSRV()
        searching = Searching(this@Home, data)
        search_recycler.adapter = searching

        db = DBOffline(this@Home)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("TAG", msg)
            // Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })


        home_recycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //change the date to the current child's date

            }
        })


        val snapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(home_recycler)




        //do all the search stuffs here
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

             /*   if (data.contains(p0)) {
                    adapter.filter.filter(p0)
                } else {
                    //Toast.makeText(this@MainActivity, "No Match found", Toast.LENGTH_LONG).show()
                }


                //check if there is a comma in the word
                //if there is a comma, check with of the 6 fields and corresponds to
                // age, gen, occ, dob, bap, loc,

                                19     > 7        <9      10 - 20
                   age ->       |        |        |          |
                              equals   greater  less       range


                */








                return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {
                shadow.visibility = when (p0!!.isNullOrEmpty()) {

                    false -> View.VISIBLE
                    true -> View.GONE
                }
                var constraint = p0!!.f()
                searching.filter.filter(p0!!)
                searching.notifyDataSetChanged()

                return true
            }


        })










        search.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {

                search_parent.setBackgroundColor(Color.parseColor("#114DFF"))
                search.visibility = View.GONE
                sort_parent.visibility = View.VISIBLE
                shadow.visibility = View.GONE


                return true;
            }


        })


        

        val progress = ProgressDialogUtil.setProgressDialog(this@Home, "Loading data...")
        progress.show()

        //getData()
        val rootRef = FirebaseDatabase.getInstance().getReference()

        Log.d("PATHS", rootRef.toString())
        var events = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                data.clear()

                for (it in snapshot.children) {

                    //var member : Members = it.getValue(Members::class.java)!!
                    data.add(
                        Members(
                            name       = it.child("name").value as String,
                            contact    = it.child("contact").value as String,
                            bday       = it.child("bday").value as String,
                            location   = it.child("location").value as String,
                            gender     = it.child("gender").value as String,
                            pic        = it.child("pic").value as String,
                            occupation = it.child("occupation").value as String,
                            baptized   = it.child("baptized").value as String,
                            sbaptized  = it.child("sbaptized").value as String,
                            mstatus    = it.child("mstatus").value as String,
                            id         = it.child("id").value as String
                        )
                    )
                }


               // db.insertData(data)


                data.sortBy { it.bday.numOfDays() }


                //data.add(data.distinct())


               // data = data.sortedWith(compareBy{it.bday})




                adapter = MemberAdapter(this@Home, data, this@Home)
                home_recycler.adapter = adapter
                adapter.notifyDataSetChanged()

                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progress.dismiss()
            }
        }

        rootRef.orderByChild("bday").addValueEventListener(events)
        rootRef.orderByChild("bday").addListenerForSingleValueEvent(events)

        //rootRef.onDisconnect()


        var sectionItemDecoration = ItemDecorationMonth(
            resources.getDimensionPixelSize(R.dimen.header_height),
            true, // true for sticky, false for not
            object : ItemDecorationMonth.SectionCallback {
                override fun isSection(position: Int): Boolean {
                    return position == 0 || data.truthy(position)
                }

                override fun getSectionHeader(position: Int): CharSequence? {
                    return data[position].bday

                }
            })
        home_recycler.addItemDecoration(sectionItemDecoration)


        titles.text = when (data.isEmpty()) {
            true -> date.format(Date())
            else -> DateUtils(data.first().bday).getMonthYear()
        }

        home_recycler.addOnScrollListener(object : OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                when (newState) {

                }

                super.onScrollStateChanged(recyclerView, newState)

            }


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visiblePosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                titles.text = DateUtils(data.get(visiblePosition).bday).getMonthYear()
            }
        })
    }

    private fun initSRV() {

        search_recycler.layoutManager = LinearLayoutManager(this@Home)
        search_recycler.itemAnimator = DefaultItemAnimator()
    }

    fun initRV(){

        home_recycler.layoutManager = LinearLayoutManager(this@Home)
        home_recycler.itemAnimator = DefaultItemAnimator()
    }

    inner class MemberHolder(itemView: View) : ViewHolder(itemView){

        var name : TextView          = itemView.name
        var image : CircleImageView  = itemView.image
        var count: TextView           = itemView.count
        var status : ImageView       = itemView.status
        var dayMonth: TextView       = itemView.mdate

    }

   inner class MemberAdapter(
       val context: Context,
       val data: ArrayList<Members>,
       val action: OnClickMember
   ) : Adapter<MemberHolder>(){
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {

           var view = LayoutInflater.from(context).inflate(R.layout.one_row_home, parent, false)
           return MemberHolder(view)
       }
       override fun onBindViewHolder(holder: MemberHolder, position: Int) {

        var member = data.get(position)!!

           var d = DateUtils(member.bday)

               when(member.pic.isNotEmpty()){

                   true ->
                       Picasso.with(this@Home)

                           .load(member.pic)
                           .error(NameUtils(member.name).getAlphaPic()!!)
                           .into(holder.image)

                   false -> holder.image.setImageResource(NameUtils(member.name).getAlphaPic()!!)


               }

           holder.name.text = member.name
           when(d.belated()){
               -1 -> {
                   holder.status.visibility = View.VISIBLE
                   holder.count.visibility = View.INVISIBLE
                   holder.dayMonth.text = d.specialDayMonth()
                   holder.name.setTextColor(Color.parseColor("#A9ACB6"))

               }

               0 -> {
                   holder.status.visibility = View.INVISIBLE
                   holder.count.visibility = View.INVISIBLE
                   holder.dayMonth.text = d.specialDayMonth()
                   holder.name.setTextColor(Color.parseColor("#0000ff"))
                   holder.count.visibility = View.VISIBLE
                   holder.count.text = " +1 "
                   holder.dayMonth.setTextColor(Color.parseColor("#0000ff"))
                   holder.dayMonth.text = "Happy Bday ${getEmojiByUnicode(0x1F382)}"


               }

               1 -> {
                   holder.status.visibility = View.INVISIBLE
                   holder.count.visibility = View.INVISIBLE
                   holder.dayMonth.text = d.specialWeek()

               }

               2 -> {
                   holder.status.visibility = View.INVISIBLE
                   holder.count.text = d.countDays()
                   holder.count.visibility = View.VISIBLE
                   holder.dayMonth.text = " ${d.specialDayMonth()} "


               }
           }

           holder.itemView.setOnClickListener {

               action.click(holder, member)
           }

       }
       override fun getItemCount() = data.size

   }


    fun onSearch(view: View){

        search.visibility = View.VISIBLE
        sort_parent.visibility = View.VISIBLE
        search_parent.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    companion object{


        lateinit var adapter : MemberAdapter
        lateinit var db: DBOffline
        lateinit var searching: Searching

        var visiblePosition = 0
        var goThere         = true
        var mAuth           = FirebaseAuth.getInstance()
        var data            = ArrayList<Members>()
        var month           = SimpleDateFormat("M")
        var date            = SimpleDateFormat("MMMM, yyyy")
    }

    fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    fun addNew(view: View){

        startActivity(Intent(this@Home, Registration::class.java))
    }

    fun ArrayList<Members>.truthy(position: Int): Boolean{

       return  when(get(position).bday.bothInSameWeek(get(position - 1).bday)){

           true -> false
           false -> {
               when (get(position).bday.same(get(position - 1).bday)) {

                   true -> false
                   false -> true
               }
           }
        }
    }

    // a date is current if the month is the current month
    fun String.current() = DateUtils(this).getMonth() == month.format(Date())!!
    fun String.bothCurrent(c1: String) = this.current() && c1.current()
    fun String.inWeek() = DateUtils(this).getWeekNumber()
    fun String.bothInSameWeek(c1: String) = this.bothCurrent(c1) && (this.inWeek() == c1.inWeek())
    fun String.same(c1: String) = DateUtils(this).getDayMonth() == DateUtils(c1).getDayMonth()

    override fun click(holder: ViewHolder, member: Members) {

        var intent = Intent(this@Home, Overview::class.java)
        intent.putExtra("members", member)
        startActivity(intent)
        finish()

    }



    inner class Searching(val context: Context, val member: ArrayList<Members>):
        Adapter<SearchingHolder>() , Filterable {
        var mFilteredList = ArrayList<Members>()
        var strings = ""
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchingHolder {

            val view = LayoutInflater.from(context).inflate(R.layout.search, parent, false)
            return SearchingHolder(view)

        }

        override fun onBindViewHolder(holder: SearchingHolder, position: Int) {

            var member = mFilteredList[position]
            holder.name.text  =  member.name

        }


       /* override fun getFilter(): Filter {

            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {

                    val charString = charSequence.toString()

                    if (charString.isEmpty()) {

                        mFilteredList = member
                    } else {
                        member?.let {
                            val filteredList = arrayListOf<Members>()
                            for (baseDataItem in member!!) {

                                    if (charString.toLowerCase(Locale.ENGLISH) in baseDataItem.name.toLowerCase(
                                            Locale.ENGLISH
                                        )){
                                        filteredList.add(baseDataItem)
                                    }
                                }


                            mFilteredList = filteredList



                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = mFilteredList
                    filterResults.count = mFilteredList.size
                    return filterResults
                }

                override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults) {
                    mFilteredList = filterResults.values as ArrayList<Members>
                    notifyDataSetChanged()

                }
            }
        }*/

        override fun getFilter(): Filter {
            return object : Filter() {


                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charString = charSequence.toString()


                    // Toast.makeText(Message.this, ""+charString, Toast.LENGTH_SHORT).show();


                    strings = charString
                    if (charString.isNullOrEmpty()) {

                        mFilteredList.clear()

                    } else {


                        var filterData = ArrayList<Members>()


                       member.filterTo(filterData){


                            it.name.toUpperCase().contains(strings.toUpperCase()) ||
                                    it.occupation!!.toUpperCase().contains(strings)


                        }







                        mFilteredList = filterData

                    }


                    var filterResults = FilterResults()

                    filterResults.count  = mFilteredList.size
                    filterResults.values = mFilteredList
                    return filterResults


                }


                override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
                ) {
                    mFilteredList =
                        filterResults.values  as ArrayList<Members>


                    // refresh the list with filtered data
                    notifyDataSetChanged()
                }

            }
        }

        override fun getItemCount() = mFilteredList.size

    }


    inner class SearchingHolder(itemView: View): ViewHolder(itemView){

        val name   : TextView = itemView.names
        val image  : CircleImageView = itemView.images
        val number : TextView = itemView.numbers
        val bases  : TextView = itemView.bases
     }


    fun ArrayList<Members>.contains(constraint : String){


    }

    fun String.f() = when{
        contains(",") -> (this.subSequence(0, this.indexOf(",")) as String).trim()
        else -> this 
    }

    fun String.s() = when{
        contains(",") -> (this.substring(this.indexOf(",")+1, lastIndex+1).trim())
        else -> ""
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

        return this.filter { DateUtils(it.bday).age() >= lower ||  DateUtils(it.bday).age() <= upper }

    }

    fun String.numOfDays(): Int {

        var d1 = DateUtils(this).countDay()
        return when {

            d1 >= 0 -> d1
            else -> 500 + d1
        }
    }




}