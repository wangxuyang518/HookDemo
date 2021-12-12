package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var mButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mButton=findViewById(R.id.mButton)
        mButton.setOnClickListener {
            Intent(MainActivity@this,MainActivity2::class.java).run{
                putExtra("startTime",System.currentTimeMillis())
                startActivity(this)
            }
        }
    }
}