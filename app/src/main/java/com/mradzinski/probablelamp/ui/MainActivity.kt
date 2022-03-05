package com.mradzinski.probablelamp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mradzinski.probablelamp.R
import com.mradzinski.probablelamp.databinding.MainActivityBinding
import com.mradzinski.probablelamp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commitNow()
        }
    }
}