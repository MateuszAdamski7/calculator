package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.lastIndexOf
import android.view.View
import android.widget.Button
import android.widget.TextView

class SimpleCalcActivity : AppCompatActivity() {

    private lateinit var equation: TextView
    private lateinit var result: TextView
    private lateinit var savedEquation: String
    private lateinit var savedResult: String
    private var canAddOperation = false
    private var canAddDecimal = true
    private var minus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calc)
        equation = findViewById<TextView>(R.id.textView4)
        result = findViewById<TextView>(R.id.textView2)

        if(savedInstanceState != null){
            savedEquation = savedInstanceState.getString("savedEquation", "")
            equation.text = savedEquation

            savedResult = savedInstanceState.getString("savedResult","")
            result.text = savedResult
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedEquation", equation.text.toString())
        outState.putString("savedResult",result.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEquation = savedInstanceState.getString("savedEquation", "")
        equation.text = savedEquation

        savedResult = savedInstanceState.getString("savedResult","")
        result.text = savedResult
    }

    fun numberAction(view: View) {
        if(view is Button){
            if(view.text == ".")
            {
                if(canAddDecimal)
                    equation.append(view.text)

                canAddDecimal = false
            }
            else
                equation.append(view.text)

            canAddOperation = true
        }
    }
    fun operationAction(view: View) {
        if(view is Button && view.text=="-" && !minus){
            equation.append(view.text)
            minus = true
            return
        }
        if(view is Button && canAddOperation)
        {
            equation.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun ACAction(view: View) {
        result.text = ""
        equation.text = ""
        canAddOperation = false
        canAddDecimal = true
        minus = false
    }
    fun CAction(view: View) {
        val length = equation.length()
        if(length > 0)
        {
            equation.text = equation.text.subSequence(0, length - 1)
        }
    }
    fun equalsAction(view: View) {
        result.text = getResult()
    }

    private fun getResult():String{
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result1 = addSubtractCalculate(timesDivision)
        return result1.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result1 = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result1 += nextDigit
                if (operator == '-')
                    result1 -= nextDigit
            }
        }

        return result1
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('*') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    '*' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        var firstDigitMinus = false
        for((index, character) in equation.text.withIndex())
        {
            if(character.equals('+') && index==0)continue
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                if (index == 0 && character == '-' && equation.text.length > 1) {
                    firstDigitMinus = true
                } else {
                    list.add(if (firstDigitMinus) -currentDigit.toFloat() else currentDigit.toFloat())
                    currentDigit = ""
                    firstDigitMinus = false
                    list.add(character)
                }
            }
        }

        if(currentDigit != "")
            list.add(if (firstDigitMinus) -currentDigit.toFloat() else currentDigit.toFloat())

        return list
    }



    fun changeSign(view: View) {
        if(equation.text.length > 0){
            var t = equation.text
            var last:Char
            try{
                last = equation.text.last { c -> c.equals('-') || c.equals('+')}
            } catch ( e:NoSuchElementException){
                last = '+'
            }

            var g = t.indexOfLast { c -> c.equals(last) }
            if(g<0){
                t = "-$t"
                equation.text = t
                return
            }
            when(last){
                '+' ->
                {
                    t = t.substring(0,g) + '-' + t.substring(g+1)
                }
                '-' ->
                {
                    if(g==0) t = t.substring(g+1)
                    else
                        t = t.substring(0,g) + '+' + t.substring(g+1)
                }
            }
            equation.text = t
        }
    }
}