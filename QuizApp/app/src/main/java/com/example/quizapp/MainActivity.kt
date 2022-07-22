package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var startButton : Button? = null

    private var tvName : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView()
    }

    private fun bindView(){
        startButton = findViewById(R.id.startButton)
        tvName = findViewById(R.id.tv_name)
        //Set on Click Listener for the button
        startButton?.setOnClickListener{
            checkValues()
        }
    }

    private fun checkValues(){
        tvName?.text.let{
            if (it!!.isEmpty()){
                Toast.makeText(this, "Please Enter your name", Toast.LENGTH_SHORT).show()
            } else {
                startquiz()
            }
        }
    }

    private fun startquiz(){
        intent = Intent(this, QuestionsActivity::class.java)
        startActivity(intent)
        finish()

    }
}