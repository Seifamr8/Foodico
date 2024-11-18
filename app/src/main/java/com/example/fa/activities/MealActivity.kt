package com.example.fa.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fa.R
import com.example.fa.database.MealDataBase
import com.example.fa.viewModel.MealViewModel
import com.example.fa.databinding.ActivityMealBinding
import com.example.fa.fragments.HomeFragment
import com.example.fa.pojo.Meal
import com.example.fa.viewModel.MealViewModelFactory


class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding:ActivityMealBinding
    private lateinit var mealMvvm:MealViewModel
    private lateinit var youtubeLink:String
    private lateinit var myMeal: Meal
    private lateinit var webView: WebView
    //private lateinit var mealVideo:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mealDataBase= MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDataBase)

        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        loadingCase()

        setInformationViews()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageCLick()


        onFavClick()
        //val rawVideoUrl =myMeal.getVideoURL()

//        val webView = findViewById<WebView>(R.id.webView)
//        val rawVideoURL=MealData.getVideoURL()
//        val videoId=rawVideoURL.substringAfter("v=").substringBefore("&")
//        val embedUrl = "https://www.youtube.com/embed/$videoId"
//        val video =
//            "<iframe width=\"100%\" height=\"100%\" src=\"$embedUrl\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
//        webView.loadData(video, "text/html", "utf-8")
//        webView.settings.javaScriptEnabled = true
//        webView.webChromeClient = WebChromeClient()

    }

    private fun onFavClick() {
        binding.btnSave.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Saved",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setupWebView(meal : Meal){
        val video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${getYoutubeCode(meal.strYoutube)}?si=9VM60AA40EQzV_Mr\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        webView.loadData(video , "text/html" , "utf-8")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
    }
    private fun getYoutubeCode(youtube : String?) : String{
        if (youtube.isNullOrBlank())
            return ""
        var i =0
        while(youtube[i] != '=')
            i++

        return youtube.substring(i+1)
    }
    private fun onYoutubeImageCLick(){
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal) {
                onResponseCase()
                val meal = t
                mealToSave=meal
                //setupWebView(meal)
                binding.tvCategoryInfo.text= "Category : ${meal!!.strCategory}"
                binding.tvAreaInfo.text="Area : ${meal.strArea}"
                binding.tvInstructions.text="Instructions: ${meal.strInstructions}"
                youtubeLink= meal.strYoutube.toString()
            }
        })
    }

    private fun  setInformationViews(){
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }


    private fun getMealInformationFromIntent(){
        val intent=intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }
    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.btnSave.visibility=View.INVISIBLE
        binding.tvInstructions.visibility=View.INVISIBLE
        binding.tvCategoryInfo.visibility=View.INVISIBLE
        binding.tvAreaInfo.visibility=View.INVISIBLE
        binding.imgYoutube.visibility=View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.btnSave.visibility=View.VISIBLE
        binding.tvInstructions.visibility=View.VISIBLE
        binding.tvCategoryInfo.visibility=View.VISIBLE
        binding.tvAreaInfo.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.VISIBLE

    }

}