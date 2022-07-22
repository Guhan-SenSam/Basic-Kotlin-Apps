package com.example.calcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var mcalcCompute = CalcCompute(this)

    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textview)
    }

    fun onPress(view: View) {
        var result: String? = null
        result = mcalcCompute.parse((view as Button).text.toString())
        result.let {
            textView?.text = it
        }
        mcalcCompute.displayString = "\n"
    }

    fun onEqual(view: View) {
        textView?.text = mcalcCompute.computeData[0].toString()
        // Now clear the calculation info and reset the calculator
        mcalcCompute.displayString = "\n"
        mcalcCompute.reset()
    }
}