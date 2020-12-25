package com.medlache.menuorderapp

import android.graphics.Bitmap

data class Food(val foodId:Int,val catId:Int,var foodName:String,var price:Int)

data class Category(val catId: Int,var catName:String,var foods:List<Food>?)

data class Order(val food:Food,var qte:Int = 1,var notes:String = "")

data class ImageOfFood(val foodId: Int,val image:Bitmap)