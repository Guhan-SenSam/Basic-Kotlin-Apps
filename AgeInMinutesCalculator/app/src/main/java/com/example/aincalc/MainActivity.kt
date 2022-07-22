package com.example.aincalc

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*





class MainActivity : AppCompatActivity() {

    var dateText : TextView? = null
    var minutesText : TextView? = null

    private fun openDatePicker(){
        var calendar = Calendar.getInstance()
        val tdyYear = calendar.get(Calendar.YEAR)
        val tdyMonth = calendar.get(Calendar.MONTH)
        val tdyDay = calendar.get(Calendar.DAY_OF_MONTH)
        var dp = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener{ view, year, month, day ->
                onDatePicked(year, month,day)
            },
            tdyYear,
            tdyMonth,
            tdyDay)
        dp.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dp.show()
    }

    private fun onDatePicked(year: Int, month: Int,day: Int ) {
        val date = "$day/${month + 1}/$year"
        date?.let{
            this.dateText = findViewById(R.id.DateText)
            this.dateText?.text = date
            Toast.makeText(this, "Date set to $day/${month + 1}/$year", Toast.LENGTH_LONG).show()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val dateobj = sdf.parse(date).time/60000
            val tdyday = sdf.parse(sdf.format(System.currentTimeMillis())).time /60000
            val difference = tdyday - dateobj
            val differenceAmount = difference.toString()
            difference?.let{
                this.minutesText = findViewById(R.id.MinutesText)
                this.minutesText?.text = differenceAmount
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dateButton : Button = findViewById(R.id.DateButton)
        dateButton.setOnClickListener {
            openDatePicker()
        }
    }
}