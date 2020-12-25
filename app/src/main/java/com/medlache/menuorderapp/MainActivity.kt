package com.medlache.menuorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.medlache.menuorderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)


        val factory = MainModelFactory("", InternetRepository.getInstance())
        viewModel = ViewModelProvider(this,factory).get(MainActivityModel::class.java)


    }

    fun getViewModel() = viewModel
}