package com.medlache.menuorderapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.medlache.menuorderapp.databinding.CartLayoutBinding
import com.medlache.menuorderapp.databinding.FoodItemBinding

class CartFragment:Fragment() {

    private lateinit var binding: CartLayoutBinding
    private lateinit var currentActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CartLayoutBinding.inflate(inflater,container,false)

        binding.deleteAllButton.setOnClickListener {
            clearCart()
        }
        binding.proceedButton.setOnClickListener {
            //send cart
            clearCart()
        }
        binding.orderButton.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentActivity = activity as MainActivity

        val cart = currentActivity.getViewModel().getCart()
        for (order in cart){
            val foodBinding = createFoodBinding(order.food,order.qte)
            foodBinding.root.id = order.food.foodId
            binding.foodsLayout.addView(foodBinding.root)
        }
    }
    private fun createFoodBinding(food:Food,qte:Int): FoodItemBinding {
        val foodBinding = FoodItemBinding.inflate(layoutInflater)
        foodBinding.foodName.text = food.foodName
        foodBinding.qteText.text = qte.toString()

        foodBinding.plusButton.setOnClickListener {
            val newQte = currentActivity.getViewModel().addToCart(food)
            foodBinding.qteText.text = newQte.toString()
        }
        foodBinding.minusButton.setOnClickListener {
            val newQte = currentActivity.getViewModel().deleteFormCart(food.foodId)
            if(newQte == 0)
                binding.foodsLayout.removeView(foodBinding.root)
            else
                foodBinding.qteText.text = newQte.toString()
        }

        foodBinding.root.id = food.foodId
        return foodBinding
    }
    private fun clearCart(){
        binding.foodsLayout.removeAllViews()
        currentActivity.getViewModel().clearCart()
    }
}