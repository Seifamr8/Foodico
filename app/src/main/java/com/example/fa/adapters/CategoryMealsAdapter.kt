package com.example.fa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fa.R
import com.example.fa.databinding.MealItemsBinding
import com.example.fa.pojo.Meal
import com.example.fa.pojo.MealsByCategory

class CategoryMealsAdapter:RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    private var mealslist = ArrayList<MealsByCategory>()
    var onItemClick: ((Meal) -> Unit)? = null // Make sure this is defined
    fun setMealsList(mealsList:List<MealsByCategory>){
        this.mealslist.clear() // Clear existing items to avoid mixing data
        this.mealslist.addAll(mealsList) // Add all items from the new list
        notifyDataSetChanged() // Notify the adapter that data has changed

    }
    inner class CategoryMealsViewModel(val binding:MealItemsBinding ):RecyclerView.ViewHolder(binding.root){
        fun bind(meal: Meal) {
            // Assuming you have a TextView and ImageView in your item layout
            itemView.findViewById<TextView>(R.id.tv_meal_name).text = meal.strMeal
            val mealImage = itemView.findViewById<ImageView>(R.id.img_meal)
            Glide.with(itemView).load(meal.strMealThumb).into(mealImage)

            // Set click listener
            itemView.setOnClickListener {
                onItemClick?.invoke(meal) // Invoke the click listener with the meal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemsBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return mealslist.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealslist[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text=mealslist[position].strMeal

    }
}
