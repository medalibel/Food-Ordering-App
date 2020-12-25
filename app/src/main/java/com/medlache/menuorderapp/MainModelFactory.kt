package com.medlache.menuorderapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class MainModelFactory(private val ipAddress:String
                       ,private val internetRepository: InternetRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityModel::class.java)){
            return MainActivityModel(ipAddress,internetRepository) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}