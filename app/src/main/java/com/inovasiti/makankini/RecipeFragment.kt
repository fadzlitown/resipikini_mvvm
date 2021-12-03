package com.inovasiti.makankini

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.inovasiti.makankini.databinding.FragmentRecipeBinding
import com.inovasiti.makankini.util.Constants.Companion.API_KEY
import com.inovasiti.makankini.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.inovasiti.makankini.util.Constants.Companion.QUERY_API_KEY
import com.inovasiti.makankini.util.Constants.Companion.QUERY_DIET
import com.inovasiti.makankini.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.inovasiti.makankini.util.Constants.Companion.QUERY_NUMBER
import com.inovasiti.makankini.util.Constants.Companion.QUERY_TYPE
import com.inovasiti.makankini.util.NetworkResult
import com.inovasiti.makankini.util.observeOnce
import com.inovasiti.makankini.MainViewModel as VM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VM
    private val mAdapter by lazy { RecipeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(VM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_recipe, container, false)
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModelLayout = viewModel

        setupRV()
        readDatabase()
        return binding.root
    }

    private fun setupRV() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.showShimmer()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipesLocal.observeOnce(viewLifecycleOwner, {
                database ->
                if(database.isNotEmpty()){
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setRecipe(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    callAPI()
                }
            })
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun callAPI() {
        Log.d("RecipesFragment", "API called!")
        viewModel.getRecipes(applyQueries())
        viewModel.recipesResponseLiveData.observe(viewLifecycleOwner, {
            response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setRecipe(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    //if there's an error from the API, then used prev data from cache if available
                    readLocalData()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.recyclerview.showShimmer()
                }
            }
        })

    }

    private fun readLocalData(){
        lifecycleScope.launch {
            viewModel.readRecipesLocal.observe(viewLifecycleOwner, {
                    database ->
                if(database.isNotEmpty()){
                    mAdapter.setRecipe(database[0].foodRecipe)
                }
            })
        }
    }

    private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // avoid memory leak
    }
}