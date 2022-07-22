package com.example.quizapp

object Constants {

    private val questionlist  = arrayListOf<Question>()

    const val CHECKANSWERS = "Check Answers"

    const val NEXTQUES = "Next Question"

    const val FINISH = "Finish the Quiz"


    fun getQuestions() : ArrayList<Question>{
        // THis function will generate all ouf questions and return them inside an array

        val q1 = Question(
            1,
            "What Country does this flag belong to?",
            R.drawable.ic_flag_of_australia,
            "Australia",
            "India",
            "Belgium",
            "Germany",
            1
        )
        val q2 = Question(
            2,
            "What Country does this flag belong to?",
            R.drawable.ic_flag_of_india,
            "Australia",
            "India",
            "Belgium",
            "Germany",
            2
        )
        val q3 = Question(
            3,
            "What Country does this flag belong to?",
            R.drawable.ic_flag_of_belgium,
            "Australia",
            "India",
            "Belgium",
            "Germany",
            3
        )
        val q4 = Question(
            4,
            "What Country does this flag belong to?",
            R.drawable.ic_flag_of_germany,
            "Australia",
            "India",
            "Belgium",
            "Germany",
            4
        )
        // Add all the questions to the array of questions
        questionlist.add(q1)
        questionlist.add(q2)
        questionlist.add(q3)
        questionlist.add(q4)
        return questionlist
    }
}