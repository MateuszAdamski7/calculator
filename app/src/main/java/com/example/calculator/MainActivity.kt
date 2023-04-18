package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleButton = findViewById<Button>(R.id.simpleButton)

        simpleButton.setOnClickListener {
            val intent = Intent(this, SimpleCalcActivity::class.java)
            startActivity(intent)
        }

        val exitButton = findViewById<Button>(R.id.exitButton)

        exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}