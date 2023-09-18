package com.mayank.workoutbookv2
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.log

class MainActivity2 : AppCompatActivity() {


    lateinit var database:AppDatabase
    private lateinit var tablayout:TabLayout
    private lateinit var view1:ConstraintLayout
    private lateinit var LV:ListView
    lateinit var workoutName:AutoCompleteTextView
    lateinit var items:List<WorkoutEntity>
    lateinit var idView1:TextView
    var day:Byte = 1

    var idd:Int = 1000

    lateinit var DeleteNameList:List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val activity = this//this two lines stops keyboard to move the UI up and down
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        database = AppDatabase.getDatabase(this)
        workoutName = findViewById(R.id.Workout)
        tablayout = findViewById(R.id.TabLayout)
        view1= findViewById(R.id.view23)
        LV = findViewById(R.id.LV)
        idView1 = findViewById(R.id.idView)
        val repsWeight:EditText = findViewById(R.id.repsInput)


        var scope1 = lifecycleScope.launch {
            database.workoutDAO().getMon().collectLatest { SyncwithDay(it) }
        }
        var scope2: Job = lifecycleScope.launch {
            database.workoutDAO().getTue().collectLatest { SyncwithDay(it) }
        }
        var scope3: Job = lifecycleScope.launch {
            database.workoutDAO().getWed().collectLatest { SyncwithDay(it) }
        }
        var scope4: Job = lifecycleScope.launch {
            database.workoutDAO().getThu().collectLatest { SyncwithDay(it) }
        }
        var scope5: Job = lifecycleScope.launch {
            database.workoutDAO().getFri().collectLatest { SyncwithDay(it) }
        }


        tablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Snackbar.make(view1, "Selected ${tab?.text}", Snackbar.LENGTH_SHORT).show()


                SyncScreen()
                if (tab != null) {
                    SyncStopper(tab.position.toByte(),scope1,scope2,scope3,scope4,scope5)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                    idView1.setText("ID")
                    workoutName.setText("")
                /*Snackbar.make(viewPager2, "selected ${tab?.text}", Snackbar.LENGTH_SHORT).show()*/
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                SyncScreen()
                when(tablayout.selectedTabPosition){
                    0 -> day =1
                    1 -> day =2
                    2 -> day =3
                    3 -> day =4
                    4 -> day =5
                }
               /*Toast.makeText(this@MainActivity2,"Selected ${tab?.text}",Toast.LENGTH_SHORT).show()*/
            }

        })

        LV.setOnItemClickListener { _, _, position, id ->
            idView1.text = "${id+1}"
            workoutName.setText(items[position].WorkoutName.toString())
            repsWeight.setText(items[position].Weight)
            idd = idView1.text.toString().toInt()-1
            SyncStopper(day,scope1,scope2,scope3,scope4,scope5)
            SyncScreen()
        }
        val dropdownText = findViewById<AutoCompleteTextView>(R.id.Workout)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.works)
        )

        // Set the adapter to the EditText view

        dropdownText.setAdapter(adapter)
        SyncScreen()

    }



    fun addItem(view:View){
        val weightERROR:TextInputLayout = findViewById(R.id.textField)
        val repsWeight:EditText = findViewById(R.id.repsInput)
        Log.d("mayankk",repsWeight.text.toString()+"hehe")
        if (workoutName.text.isBlank() || repsWeight.text.isBlank()){
            workoutName.error = "stop entering blank"
            return
        }


        else if(!repsWeight.text.toString()[0].isDigit() || !repsWeight.text.toString()[1].isDigit() ){
            repsWeight.error = "format wrong"
            repsWeight.setText("")
            return
        }




        database = AppDatabase.getDatabase(this)
        if (idView1.text =="ID")
        {
        GlobalScope.launch {
           database.workoutDAO().insertAll(WorkoutEntity(0, workoutName.text.toString(),repsWeight.text.toString(),day))
            SyncScreen()          }
        }
        else{
            GlobalScope.launch {
                database.workoutDAO().insertAll(WorkoutEntity(DeleteNameList[idd].toInt(), workoutName.text.toString(),repsWeight.text.toString(),day))
                idView1.text ="ID"
                workoutName.setText("")
                SyncScreen()
            }

        }

    }

    fun SyncScreen(){

        when(tablayout.selectedTabPosition){
            0 -> day =1
            1 -> day =2
            2 -> day =3
            3 -> day =4
            4 -> day =5
        }

//        if (day==1.toByte()){
//            database.workoutDAO().getMon().observe(this) {
//             SyncwithDay(it)}
//        }



        if (day==1.toByte()){

               lifecycleScope.launch {

                database.workoutDAO().getMon().collectLatest {
                    SyncwithDay(it)}
            }

        }



        else if (day==2.toByte()){

           lifecycleScope.launch {
            database.workoutDAO().getTue().collectLatest {
                if (day!=2.toByte()){this.cancel()}
                SyncwithDay(it)
              }
           }
        }
        else if (day==3.toByte()){
             lifecycleScope.launch {
                database.workoutDAO().getWed().collectLatest {
                    SyncwithDay(it)}}
        }
        else if (day==4.toByte()){
             lifecycleScope.launch {
                database.workoutDAO().getThu().collectLatest {
                    SyncwithDay(it)}}
        }
        else if (day==5.toByte()){
            lifecycleScope.launch {

                database.workoutDAO().getFri().collectLatest {
                    if (day!=5.toByte()){this.cancel()}
                    SyncwithDay(it)}}
        }



    }

    fun SyncwithDay(it:List<WorkoutEntity>){

        items = it



            DeleteNameList = items.map { "${it.id}" }
           val workoutNameList = items.map { it -> "${it.WorkoutName}${" ".repeat(100 - it.WorkoutName!!.length)}${it.Weight}" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, workoutNameList)
            LV.adapter = adapter



    }




    fun deleteItem(view: View){

        if (idd>500){return}
        else if (idView1.text.toString() == "ID"){return}



        //        Log.d("mannn", "deleteItem: ${items[databaseID]} ")
       GlobalScope.launch{


          database.workoutDAO().delete(items[idd])
            //converting list positon to id of the data according to table in db for delete
           idView1.text ="ID"
           workoutName.setText("")
           SyncScreen()


      }
    }

    fun SyncStopper(positiontab:Byte , scope1:Job , scope2:Job , scope3:Job , scope4:Job, scope5:Job){



        if (positiontab==1.toByte()){
            if (scope2.isActive){scope2.cancel()}
            if (scope3.isActive){scope3.cancel()}
            if (scope4.isActive){scope4.cancel()}
            if (scope5.isActive){scope5.cancel()}
        }
        else if (positiontab==2.toByte()){
            if (scope1.isActive){scope1.cancel()}
            if (scope3.isActive){scope3.cancel()}
            if (scope4.isActive){scope4.cancel()}
            if (scope5.isActive){scope5.cancel()}
        }
        else if (positiontab==3.toByte()){
            if (scope1.isActive){scope1.cancel()}
            if (scope2.isActive){scope2.cancel()}
            if (scope4.isActive){scope4.cancel()}
            if (scope5.isActive){scope5.cancel()}
        }
        else if (positiontab==4.toByte()){
            if (scope1.isActive){scope1.cancel()}
            if (scope3.isActive){scope3.cancel()}
            if (scope2.isActive){scope2.cancel()}
            if (scope5.isActive){scope5.cancel()}
        }

    }


}
