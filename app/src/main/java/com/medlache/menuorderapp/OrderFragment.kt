package com.medlache.menuorderapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.medlache.menuorderapp.databinding.FoodCategoryBinding
import com.medlache.menuorderapp.databinding.FoodItemBinding
import com.medlache.menuorderapp.databinding.OrderLayoutBinding
import com.medlache.menuorderapp.MainActivityModel.FoodsResponse

class OrderFragment : Fragment() {

    private lateinit var binding:OrderLayoutBinding
    private lateinit var currentActivity: MainActivity
    private val foodsObserver = Observer{foodsResponse:FoodsResponse ->
        binding.foodsProgress.visibility = View.GONE
        Log.i("orderFragment","exe observer method")
        if(!foodsResponse.result.success){
            //show retry button and error text
            binding.errorMessage.visibility = View.VISIBLE
            binding.retryButton.visibility = View.VISIBLE
            //error text
            //binding.errorMessage.text = foodsResponse.result.message
        }else{
            addCategories(foodsResponse.foods)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderLayoutBinding.inflate(inflater,container,false)

        binding.cartButton.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_orderFragment_to_cartFragment)
        }
        binding.retryButton.setOnClickListener {
            requestFoods()

            binding.errorMessage.visibility = View.GONE
            it.visibility = View.GONE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("orderFragment","on activity created")

        currentActivity = activity as MainActivity
        val activityViewModel = currentActivity.getViewModel()

        requestFoods()
        val observer = Observer{foodsResponse:FoodsResponse ->
            binding.foodsProgress.visibility = View.GONE
            Log.i("orderFragment","exe observer method")
            if(!foodsResponse.result.success){
                //show retry button and error text
                binding.errorMessage.visibility = View.VISIBLE
                binding.retryButton.visibility = View.VISIBLE
                //error text
                //binding.errorMessage.text = foodsResponse.result.message
            }else{
                addCategories(foodsResponse.foods)
            }
        }

        activityViewModel.liveFoodsResponse.observe(this.viewLifecycleOwner,observer)

    }

    private fun requestFoods(){
        currentActivity.getViewModel().requestFoods()
        binding.foodsProgress.visibility = View.VISIBLE
    }
    private fun addCategories(foodsCategories:ArrayList<Category>){
        binding.categoriesContainer.removeAllViews()
        for(category in foodsCategories){
            if(category.foods != null){
                val catBinding = createCategoryBinding(category)

                for (food in category.foods!!){
                    val foodBinding = createFoodBinding(food)
                    addFoodViewToCategory(foodBinding.root,catBinding)
                }
                binding.categoriesContainer.addView(catBinding.root)
            }
        }
    }
    private fun createCategoryBinding(category:Category):FoodCategoryBinding{
        val catBinding = FoodCategoryBinding.inflate(layoutInflater)
        catBinding.categoryTitle.text = category.catName
        catBinding.root.id = category.catId

        return catBinding
    }
    private fun createFoodBinding(food:Food):FoodItemBinding{
        val foodBinding = FoodItemBinding.inflate(layoutInflater)
        foodBinding.foodName.text = food.foodName
        foodBinding.qteText.text = currentActivity.getViewModel().getFoodQte(food.foodId).toString()

        foodBinding.plusButton.setOnClickListener {
            val qte = currentActivity.getViewModel().addToCart(food)
            foodBinding.qteText.text = qte.toString()
        }
        foodBinding.minusButton.setOnClickListener {
            val qte = currentActivity.getViewModel().deleteFormCart(food.foodId)
            foodBinding.qteText.text = qte.toString()
        }

        foodBinding.root.id = food.foodId
        return foodBinding
    }
    private fun addFoodViewToCategory(foodView:View,catBinding:FoodCategoryBinding){
        catBinding.foodsContainer.addView(foodView)
    }
}