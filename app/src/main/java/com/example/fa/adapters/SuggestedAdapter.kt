package com.example.fa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fa.databinding.SuggestedBinding
import com.example.fa.pojo.MealsByCategory

class SuggestedAdapter():RecyclerView.Adapter<SuggestedAdapter.SugesstedViewHolder>(){
    lateinit var onItemClick:((MealsByCategory)->Unit)
    private var mealsList=ArrayList<MealsByCategory>()
    fun setMeals(mealsList: ArrayList<MealsByCategory>){
        this.mealsList=mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugesstedViewHolder {
        return SugesstedViewHolder(SuggestedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: SugesstedViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgSuggested)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }
    }
    class SugesstedViewHolder( val binding: SuggestedBinding):RecyclerView.ViewHolder(binding.root)

}