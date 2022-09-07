package com.example.capstone2test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2test.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}