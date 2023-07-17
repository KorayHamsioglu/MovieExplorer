package com.example.obss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var buttonEnter: Button
    private lateinit var buttonRetry: Button
    private lateinit var editTextNumber: EditText
    private lateinit var textViewInfo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonEnter=findViewById(R.id.buttonEnter)
        buttonRetry=findViewById(R.id.buttonRetry)
        editTextNumber=findViewById(R.id.editText)
        textViewInfo=findViewById(R.id.textView)

        val randomNumber=randomNumberGenerator()
        var count: Int = 0

        buttonEnter.setOnClickListener {
            val number=editTextNumber.text.toString().toIntOrNull()
            count++
            if (count==5 && number!=randomNumber){
                editTextNumber.visibility= View.INVISIBLE
                buttonEnter.visibility=View.INVISIBLE
                textViewInfo.text="You lost, try again."
                buttonRetry.visibility=View.VISIBLE
            }else{
                if (number!=null){
                    if(number<randomNumber){
                        textViewInfo.text="Please enter a larger number."
                    }else if(number>randomNumber){
                        textViewInfo.text="Please enter a smaller number."
                    }else{
                        editTextNumber.visibility= View.INVISIBLE
                        buttonEnter.visibility=View.INVISIBLE
                        textViewInfo.text="Congratulations, you won!"
                        buttonRetry.visibility=View.VISIBLE
                    }
                }else{
                    textViewInfo.text="Please enter a valid number."
                }
            }

        }

        buttonRetry.setOnClickListener{
            recreate()
        }
    }


    fun randomNumberGenerator(): Int {
        return Random.nextInt(101)
    }
}