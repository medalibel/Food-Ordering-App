package com.medlache.menuorderapp

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class InternetRepository private constructor() {
    //private val executorService:ExecutorService = Executors.newFixedThreadPool(3)
    private val mainThreadHandler:Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object{
        @Volatile
        private var instance:InternetRepository? = null

        @Synchronized
        fun getInstance():InternetRepository{
            var _instance = instance
            if(_instance == null){
                _instance = InternetRepository()
                instance = _instance
            }
            return _instance
        }

    }
    @Volatile
    private var gettingData = false

    data class Response(var success:Boolean,var message:String = "")

    fun sendCart(baseAddress:String){

        thread {
            // sending logic

            //callback method

        }
    }
    fun getFoods(baseAddress:String,callback: (data:ArrayList<Category>,result:Response) -> Unit) {

        synchronized(this){
            if(!gettingData){
                Log.i("Internet","launching a thread !!!!")
                thread {
                    synchronized(this){
                        gettingData = true
                    }

                    requestFoods(baseAddress)
                    val foodsByCat = ArrayList<Category>()
                    foodsByCat.add(
                        Category(
                            1,
                            "Pizza",
                            listOf(
                                Food(1, 1, "Pizza Cheeze", 1000),
                                Food(2, 1, "Pizza Meat", 800),
                                Food(3, 1, "Pizza Simple", 100)
                            )
                        )
                    )
                    foodsByCat.add(Category(2,
                        "Tacos",
                        listOf(Food(4,2,"Tacos Mixte",400),
                            Food(5,2,"Tacos Poulet",250),
                            Food(6,2,"Tacos Meat",350))
                    ))
                    foodsByCat.add(Category(3,
                        "Burger",
                        listOf(Food(7,3,"Burger Poulet",150),
                            Food(8,3,"Burger Double Cheeze",300),
                            Food(9,3,"Burger Meat",250))
                    ))
                    mainThreadHandler.post{
                        callback(foodsByCat,Response(false))
                    }

                    synchronized(this){
                        gettingData = false
                    }
                }
            }
        }


    }
    private fun requestFoods(baseAddress: String){
        // make a network request for foods
        //okhttp
        Thread.sleep((2000))

    }

}