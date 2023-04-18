package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_simple = findViewById<Button>(R.id.button_simple)

        button_simple.setOnClickListener {
            Toast.makeText(this, "Przycisk został kliknięty", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SimpleCalcActivity::class.java)
            startActivity(intent)
        }
    }
}