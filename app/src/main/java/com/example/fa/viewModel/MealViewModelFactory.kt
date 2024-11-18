package com.example.fa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fa.database.MealDataBase

class MealViewModelFactory(
    private val mealDataBase: MealDataBase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDataBase) as T
    }
}