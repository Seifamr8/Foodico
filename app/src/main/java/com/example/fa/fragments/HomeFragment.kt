package com.example.fa.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fa.R
import com.example.fa.activities.CategoryMealsActivity
import com.example.fa.activities.MainActivity
import com.example.fa.activities.MealActivity
import com.example.fa.adapters.CategoriesAdapter
import com.example.fa.adapters.SuggestedAdapter
import com.example.fa.database.MealDataBase
import com.example.fa.databinding.FragmentsHomeBinding
import com.example.fa.pojo.MealsByCategory
import com.example.fa.pojo.Meal
import com.example.fa.viewModel.HomeViewModel
import com.example.fa.viewModel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentsHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var suggestedItemsAdapter: SuggestedAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.fa.fragments.idMeal"
        const val MEAL_NAME = "com.example.fa.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.fa.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.fa.fragments.categoryName"
    }

    // HomeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealDataBase = MealDataBase.getInstance(requireContext())
        val factory = HomeViewModelFactory(mealDataBase)
        homeMvvm = (activity as MainActivity).viewModel
        suggestedItemsAdapter = SuggestedAdapter() // Fix typo
    }

// Correct usage of suggestedItemsAdapter in other places


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentsHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecycler()

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMvvm.getSuggestedItems()
        observeSuggestedItemLiveData()
        onSuggestedClick()

        prepareCategoriesRecycler()
        homeMvvm.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
        onSearchIconClick()
        binding.imgFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favouritesFragment)
        }

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_searchFragment)        }
    }


    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecycler() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        homeMvvm.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoriesList(categories)
        }
    }

    private fun onSuggestedClick() {
        suggestedItemsAdapter.onItemClick = { randomMeal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecycler() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = suggestedItemsAdapter
        }
    }

    private fun observeSuggestedItemLiveData() {
        homeMvvm.observeSuggestedItemsLiveData().observe(viewLifecycleOwner,
            {mealList->
                suggestedItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)

            })
    }


    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal(){
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal=meal
        }
    }

}
