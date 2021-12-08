package com.example.grocery_comparator.compareLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.grocery_comparator.R

class ComparePrice : AppCompatActivity() {
    private lateinit var demoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_price)

        demoText = findViewById(R.id.result)


    }
}