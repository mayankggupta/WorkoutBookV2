package com.mayank.workoutbookv2

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    val currentNightMode = AppCompatDelegate.getDefaultNightMode()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun check(view:View){
        var name:TextInputEditText
        var intent:Intent
        val handler = Handler(Looper.getMainLooper()) //this is used for adding delay
        name = findViewById(R.id.nametext)
        val contextView = findViewById<View>(R.id.view2)

        Snackbar.make(contextView, name.text.toString().lowercase().capitalize()+" Welcome", Snackbar.LENGTH_SHORT).show()
        //snackbar is lika a toast

        intent = Intent(this, MainActivity2::class.java)

        handler.postDelayed({
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)//this clears previous activity
            startActivity(intent) //adding delay for changing activity and show a toast
        }, 100)


    }

    fun themeModeBtn(view: View){
// Toggle the night mode.


        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }

// Recreate the activity to apply the new night mode.
        recreate()
    }
}