package com.example.fa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fa.database.MealDataBase

class HomeViewModelFactory(private val mealDatabase: MealDataBase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mealDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
