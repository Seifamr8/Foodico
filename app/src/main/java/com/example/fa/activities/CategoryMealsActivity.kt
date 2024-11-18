package com.example.fa.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fa.viewModel.CategoryMealsViewModel
import com.example.fa.adapters.CategoryMealsAdapter
import com.example.fa.databinding.ActivityCategoryMealsBinding
import com.example.fa.fragments.HomeFragment
import com.example.fa.pojo.Meal
import com.example.fa.activities.MealActivity

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        // Initialize ViewModel
        categoryMealsViewModel = ViewModelProvider(this).get(CategoryMealsViewModel::class.java)

        // Get category name from intent
        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME) ?: return
        categoryMealsViewModel.getMealsByCategory(categoryName)

        // Observe meals LiveData
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = "Meals count : ${mealsList.size}"
            mealsList.forEach {
                Log.d("test", it.strMeal)
            }
            categoryMealsAdapter.setMealsList(mealsList)
        })

        // Handle item clicks
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java).apply {
                putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}
