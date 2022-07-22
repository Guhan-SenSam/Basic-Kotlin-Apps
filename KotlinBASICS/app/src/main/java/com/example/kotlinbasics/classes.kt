package com.example.kotlinbasics

class MobilePhone(osName : String = "default", brand : String= "default", model: Int= 0){

    var battery = 30

    init{
        print("Mobile Brand: $brand\n"+"Mobile OS: $osName"+ "Model no: $model")
    }

    fun chargeBattery(percent : Int){
        print("The original Battery was ${this.battery}")
        print("The Battery will be increased by $percent")
        this.battery += percent
        print("The New Battery is ${this.battery}")
    }
}

fun main(){
    var obj = MobilePhone()
    obj.chargeBattery(25)

}

