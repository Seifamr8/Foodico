package com.example.fa.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fa.R
import com.example.fa.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.fa.databinding.FragmentsSearchBinding
import com.example.foodieapp.adapters.MealsAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentsSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use ViewModelProvider to get the HomeViewModel
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentsSearchBinding.inflate(inflater)
        return binding.root
        return inflater.inflate(R.layout.fragments_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imageSearchArrow.setOnClickListener { searchMeals() }

        observeSearchedMealsLiveData()

        var searchJob: Job? = null
        binding.searchBar.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(100) // Add delay to prevent instant searching while typing
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner) { mealsList ->
            if (mealsList.isNullOrEmpty()) {
                Log.d("SearchFragment", "No meals found")
            } else {
                Log.d("SearchFragment", "Meals found: ${mealsList.size}")
                searchRecyclerViewAdapter.differ.submitList(mealsList)
            }
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.searchBar.text.toString()
        if (searchQuery.isNotEmpty()) {
            Log.d("SearchFragment", "Searching meals with query: $searchQuery")
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
}
