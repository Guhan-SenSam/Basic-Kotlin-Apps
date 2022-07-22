package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener{

    private var questionNumTv : TextView? = null
    private var questionTv : TextView? = null
    private var questionImage : ImageView? = null
    private var option1Tv : TextView? = null
    private var option2Tv : TextView? = null
    private var option3Tv : TextView? = null
    private var option4Tv : TextView? = null
    private var prgBar : ProgressBar? = null
    private var prgTv : TextView? = null
    private var nxtButton : Button? = null

    private var questionslist = Constants.getQuestions()
    private var currentQuestion = 0
    private var answerShown = false
    private var selectedAnswer = 0

    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        bindView()
        loadView()
    }

    private fun bindView(){
    // This function binds all elements in the question view
        questionNumTv = findViewById(R.id.question_num_tv)
        questionTv = findViewById(R.id.question_text_tv)
        questionImage = findViewById(R.id.question_img)
        option1Tv = findViewById(R.id.tv_button1)
        option2Tv = findViewById(R.id.tv_button2)
        option3Tv = findViewById(R.id.tv_button3)
        option4Tv = findViewById(R.id.tv_button4)
        prgBar = findViewById(R.id.prg_bar)
        prgTv = findViewById(R.id.prg_text)
        nxtButton = findViewById(R.id.nxt_button)
        nxtButton?.setOnClickListener{
            buttonClick()
        }
        // Set the onlick listeners for the four textview
        option1Tv?.setOnClickListener(this)
        option2Tv?.setOnClickListener(this)
        option3Tv?.setOnClickListener(this)
        option4Tv?.setOnClickListener(this)

    }

    private fun loadView(){
        //This function will load the next question when it is executed
        //Set all the view elements to their respective data
        questionNumTv?.text = "Question ${questionslist[currentQuestion].id}"
        questionTv?.text = questionslist[currentQuestion].question
        questionImage?.setImageResource(questionslist[currentQuestion].picture)
        option1Tv?.text = questionslist[currentQuestion].option1
        option2Tv?.text = questionslist[currentQuestion].option2
        option3Tv?.text = questionslist[currentQuestion].option3
        option4Tv?.text = questionslist[currentQuestion].option4
        nxtButton?.text = Constants.CHECKANSWERS
        // Set the progress bars properly
        prgBar?.progress = currentQuestion+1
        prgTv?.text = "${currentQuestion+1}/4"
        if (currentQuestion == 4){
            nxtButton?.text = Constants.FINISH
        }
    }

    private fun buttonClick(){
        // This function is called on clicking of the main button
        // this function decides whether we should move to the next question or show the answer
        // if we are on our last question we toast that the app is finished
        if (!answerShown){
            // Show the answers
            setDefaultView()
            showAnswers()
            answerShown = true
        } else {
            // Move to the next questions
            setDefaultView()
            currentQuestion++
            loadView()
            answerShown = false
        }
        if (currentQuestion == 3){
            Toast.makeText(this, "Your Score:${score}/4", Toast.LENGTH_LONG).show()
        }

    }

    private fun setDefaultView(){
        option1Tv?.setBackgroundResource(R.drawable.tv_bg_normal)
        option2Tv?.setBackgroundResource(R.drawable.tv_bg_normal)
        option3Tv?.setBackgroundResource(R.drawable.tv_bg_normal)
        option4Tv?.setBackgroundResource(R.drawable.tv_bg_normal)
    }

    private fun setSelected(id : Int){
        when(id){
            R.id.tv_button1 -> {
                selectedAnswer = 1
                option1Tv?.setBackgroundResource(R.drawable.tv_bg_active)
            }
            R.id.tv_button2 -> {
                selectedAnswer = 2
                option2Tv?.setBackgroundResource(R.drawable.tv_bg_active)
            }
            R.id.tv_button3 -> {
                selectedAnswer = 3
                option3Tv?.setBackgroundResource(R.drawable.tv_bg_active)
            }
            R.id.tv_button4 -> {
                selectedAnswer = 4
                option4Tv?.setBackgroundResource(R.drawable.tv_bg_active)
            }
        }

    }

    private fun showAnswers(){
        // This function will check if the entered answer is correct, display the correct answer
        // and increment the score
        if (selectedAnswer == questionslist[currentQuestion].answer){
            //answer is right
            score ++
        } else {
            // Show the wrong answer color
            when(selectedAnswer){
                1 -> option1Tv?.setBackgroundResource(R.drawable.tv_bg_wrong)
                2 -> option2Tv?.setBackgroundResource(R.drawable.tv_bg_wrong)
                3 -> option3Tv?.setBackgroundResource(R.drawable.tv_bg_wrong)
                4 -> option4Tv?.setBackgroundResource(R.drawable.tv_bg_wrong)
            }
        }
        // We then just show the correct option regardless
        when(questionslist[currentQuestion].answer){
            1 -> option1Tv?.setBackgroundResource(R.drawable.tv_bg_right)
            2 -> option2Tv?.setBackgroundResource(R.drawable.tv_bg_right)
            3 -> option3Tv?.setBackgroundResource(R.drawable.tv_bg_right)
            4 -> option4Tv?.setBackgroundResource(R.drawable.tv_bg_right)
        }
        nxtButton?.text = Constants.NEXTQUES

    }

    override fun onClick(v: View?) {
        // Handles the onclick method for the four textview buttons we have
            // This means we have to call the function to display answer selection
            setDefaultView()
            setSelected((v as TextView).id)
    }
}