package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.cos
import java.lang.Math.pow
import kotlin.math.*

class AdvancedCalcActivity : AppCompatActivity() {

    private lateinit var equation: TextView
    private lateinit var result: TextView
    private lateinit var savedEquation: String
    private lateinit var savedResult: String
    private var canAddOperation = false
    private var canAddDecimal = true
    private var minus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_calc)

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
        val t = equation.text
        if('%' in t || "sin" in t || "cos" in t || "tan" in t || "ln" in t || "sqrt" in t || "^" in t || "log" in t){
            val temp:String = getAdResult()
            result.text = getResult(temp)
            return
        }
        result.text = getResult("")
        if(result.text.endsWith(".0")){
            result.text = result.text.subSequence(0,result.text.length-2)
        }
    }
    fun extractLastFloatNumber(input: String): Float? {
        val regex = Regex("\\d*\\.?\\d+?")
        val matchResult = regex.findAll(input).toList().lastOrNull()
        return matchResult?.value?.toFloatOrNull()
    }
    fun extractLastFloatNumberAfter(input: String, exp: String): String {
        val regex = Regex("$exp(\\d*\\.?\\d+?)")
        val matchResult = regex.findAll(input).toList().lastOrNull()
        return matchResult?.groupValues?.last().toString()
    }
    fun extractLastFloatNumberBefore(input: String, exp: String): String {
        val regex = Regex("(\\d*\\.?\\d+?)$exp")
        val matchResult = regex.findAll(input).toList().lastOrNull()
        return matchResult?.groupValues?.last().toString()
    }

    private fun getAdResult():String{
        var t = equation.text.toString()
        var num1:Float? = null
        var num2:Float? = null
        while('%' in t || "sin" in t || "cos" in t || "tan" in t || "ln" in t || "sqrt" in t || "^" in t || "log" in t)
        {
            if('%' in t){
                val temp:String = extractLastFloatNumberBefore(t,"%")
                num1 = temp.toFloatOrNull()
                if (num1 != null) {
                    num2 = num1/100
                }
                else{
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }

                t = t.replace("$temp%",num2.toString())
            }
            else if("sin" in t){
                val temp:String = extractLastFloatNumberAfter(t,"sin")
                num1 = temp.toFloatOrNull()
                val res = num1?.let { sin(it) }
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                t = t.replace("sin$temp",res.toString())
            }
            else if("cos" in t){
                val temp:String = extractLastFloatNumberAfter(t,"cos")
                num1 = temp.toFloatOrNull()
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { cos(it.toDouble()) }
                t = t.replace("cos$temp",res.toString())
            }
            else if("tan" in t){
                val temp:String = extractLastFloatNumberAfter(t,"tan")
                num1 = temp.toFloatOrNull()
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { tan(it) }
                t = t.replace("tan$temp",res.toString())
            }
            else if("sqrt" in t){
                val temp:String = extractLastFloatNumberAfter(t,"sqrt")
                num1 = temp.toFloatOrNull()
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { sqrt(it) }
                t = t.replace("sqrt$temp",res.toString())
            }
            else if("ln" in t){
                val temp:String = extractLastFloatNumberAfter(t,"ln")
                num1 = temp.toFloatOrNull()
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { ln(it) }
                t = t.replace("ln$temp",res.toString())
            }
            else if("log" in t){
                val temp:String = extractLastFloatNumberAfter(t,"log")
                num1 = temp.toFloatOrNull()
                if(temp.isEmpty() || num1 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { log(it.toDouble(), 10.0) }
                t = t.replace("log$temp",res.toString())
            }
            else if("^" in t){
                val temp1:String = extractLastFloatNumberBefore(t,"\\^")
                val temp2:String = extractLastFloatNumberAfter(t,"\\^")
                num1 = temp1.toFloatOrNull()
                num2 = temp2.toFloatOrNull()
                if(temp1.isEmpty() || temp2.isEmpty() || num1 == null|| num2 == null){
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe działanie", Toast.LENGTH_SHORT).show();
                    return "error"
                }
                val res = num1.let { num2.let { it1 -> pow(it.toDouble(), it1.toDouble()) } }
                t = t.replace("$temp1^$temp2",res.toString())
            }
        }

        return t
    }

    private fun getResult(eqstring: String?):String{
        if (eqstring=="error") return ""
        val digitsOperators = digitsOperators(eqstring.toString())
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

    private fun digitsOperators(eqstring: String): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        var firstDigitMinus = false
        var tempEq:String = eqstring
        if (tempEq.isEmpty()) tempEq=equation.text.toString()
        if(tempEq.isEmpty()) return list
        for((index, character) in tempEq.withIndex())
        {
            if(character.equals('-') && firstDigitMinus){
                list.add(if (firstDigitMinus) -currentDigit.toFloat() else currentDigit.toFloat())
                currentDigit = ""
                list.add('+')
                continue

            }
            if(character.equals('+') && index==0)continue
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                if ((index == 0 && character == '-' && equation.text.length > 1) || (list.isNotEmpty() && character == '-' && (list.last() == '/' || list.last() == '*'))) {
                    firstDigitMinus = true
                }
                else
                {
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
                last = equation.text.last { c -> c.equals('-') || c.equals('+') || c.equals('/') || c.equals('*')}
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
                '/' ->
                {
                    if(g==0) t = t.substring(g+1)
                    else
                        t = t.substring(0,g+1) + '-' + t.substring(g+1)
                }
                '*' ->
                {
                    if(g==0) t = t.substring(g+1)
                    else
                        t = t.substring(0,g+1) + '-' + t.substring(g+1)
                }

            }
            equation.text = t
        }
    }

    fun percentAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun sinAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun cosAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun tanAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun lnAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun sqrtAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
    fun pow2Action(view: View) {
        if(view is Button)
            equation.append("^2")
    }
    fun powAction(view: View) {
        if(view is Button)
            equation.append("^")
    }
    fun logAction(view: View) {
        if(view is Button)
            equation.append(view.text)
    }
}