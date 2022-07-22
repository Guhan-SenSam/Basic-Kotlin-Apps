package com.example.calcapp

import android.app.Activity
import android.widget.Toast

class CalcCompute (context : Activity){

    private val activity = context

    val data = mutableListOf<String>()

    val computeData = mutableListOf<String>()

    var displayString : String? = "\n"

    private val OPERATIONS = arrayOf("/","*","-","+")

    private var currentAnswer: Float? = null

    fun parse(text: String) : String?{
        //check if it is our first element and allow only numbers to be added first
        if (this.data.size == 0){
            if (text !in  this.OPERATIONS){
                data.add(text)
            } else{
                Toast.makeText(this.activity, "Enter A number First", Toast.LENGTH_SHORT).show()
                return null
            }
        } else {
            // If it is not our first element we then check if it is another number
            //if another number then append the string to the previous list
            //entry string. Else append the new operation
            if (text !in this.OPERATIONS){
                //Now we check to make sure the last element was a number. If not then we add the new
                //text as another element of the list
                if (data[data.lastIndex] in this.OPERATIONS){
                    data.add(text)
                } else {
                    data[data.lastIndex] = data.last() + text
                }
            } else {
                // Check if the last entered value is already an operator
                if (data[data.lastIndex] in this.OPERATIONS){
                    Toast.makeText(this.activity, "Syntax Error", Toast.LENGTH_SHORT).show()
                } else{
                    data.add(text)
                }
            }
        }
        // Copy the contents of the parsed string into our display variable
        // Now we call the calculate function to calculate the value of our current data
        // This function should only be called if it is a number being entered and it is not
        // the first number being entered
        this.computeData.clear()
        this.computeData.addAll(this.data)
        createDisplayString()
        if (this.data.size != 1 && text !in this.OPERATIONS){
            this.currentAnswer = calculate()
            // Now generate the final display string
            this.displayString = this.currentAnswer.toString() + this.displayString
            return this.displayString
        }
        if (this.currentAnswer ==  null){
            return "0" + this.displayString
        }
            return  this.currentAnswer.toString() + this.displayString
    }

    private fun createDisplayString(){
        for (i in this.data){
            displayString += i
        }
    }

    private fun checkIfOperation() : Boolean {
        for (ele in this.computeData){
            if(ele in this.OPERATIONS){
                return true
            }
        }
        return false
    }

    private fun calculate() : Float?{
        var before : Float
        var after : Float
        while (checkIfOperation()){
            for (i in 1 until this.computeData.size-1){
                if (this.computeData[i] in this.OPERATIONS){
                    before = this.computeData[i-1].toFloat()
                    after = this.computeData[i+1].toFloat()
                    if (this.computeData[i] == "/"){
                        this.computeData[i-1] = (before / after).toString()
                        this.computeData.removeAt(i+1)
                        this.computeData.removeAt(i)
                        break

                    } else if (this.computeData[i] == "*"){
                        this.computeData[i-1] = (before * after).toString()
                        this.computeData.removeAt(i+1)
                        this.computeData.removeAt(i)
                        break
                    } else if (this.computeData[i] == "-"){
                        this.computeData[i-1] = (before - after).toString()
                        this.computeData.removeAt(i+1)
                        this.computeData.removeAt(i)
                        break
                    } else if (this.computeData[i] == "+"){
                        this.computeData[i-1] = (before + after).toString()
                        this.computeData.removeAt(i+1)
                        this.computeData.removeAt(i)
                        break
                    }
                }
            }
        }
    return this.computeData[0].toFloat()
    }

    fun reset(){
        this.data.clear()
        this.computeData.clear()
    }
}