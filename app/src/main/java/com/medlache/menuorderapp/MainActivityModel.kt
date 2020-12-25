package com.medlache.menuorderapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityModel(private var ipAddress:String
                        ,private var internetRepository: InternetRepository):ViewModel() {

    data class FoodsResponse(var foods:ArrayList<Category>,var result:InternetRepository.Response)

    private var cart : ArrayList<Order> = ArrayList(10)
    var liveFoodsResponse : MutableLiveData<FoodsResponse> = MutableLiveData()
    private var foodsByCat : ArrayList<Category> = ArrayList(10)
    private var images = ArrayList<ImageOfFood>(10)
    var liveImage = MutableLiveData<ImageOfFood>()

    fun requestFoods() {
        val foodsResponse = FoodsResponse(foodsByCat, InternetRepository.Response(true))
        if(foodsByCat.isEmpty()){
            internetRepository.getFoods(ipAddress){foodsData ,result->
                if(result.success){
                    foodsByCat = foodsData
                    foodsResponse.foods = foodsByCat

                }else{
                    foodsResponse.result.success = false
                    foodsResponse.result.message = result.message

                }
                liveFoodsResponse.value = foodsResponse
            }
            return
        }
        liveFoodsResponse.value = foodsResponse
    }
    fun getCart():List<Order>{
        return cart
    }

    fun sendCart(){
        //internetRepository.sendCart()
    }
    fun clearCart(){
        cart.clear()
    }
    fun addOrderNote(foodId:Int,note:String){
        val order = findOrderOrNull(foodId)
        if(order != null)
            order.notes = note
    }
    fun addToCart(food:Food):Int{

        var order = findOrderOrNull(food.foodId)
        if(order != null){
            order.qte += 1
            return order.qte
        }
        order = Order(food)
        cart.add(order)
        return order.qte
    }
    fun deleteFormCart(foodId: Int) :Int{

        val order = findOrderOrNull(foodId)
        if(order != null){
            order.qte -= 1
            if(order.qte <= 0) {
                cart.remove(order)
                return 0
            }
            return order.qte
        }
        return 0
    }
    fun getFoodQte(foodId:Int):Int{
        val order = findOrderOrNull(foodId)
        if(order != null)
            return order.qte

        return 0
    }
    private fun findOrderOrNull(foodId :Int):Order?{
        for(order in cart){
            if(order.food.foodId == foodId)
                return order
        }
        return null
    }
}