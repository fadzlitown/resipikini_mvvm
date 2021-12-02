package com.inovasiti.makankini

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.inovasiti.makankini.util.Constants.Companion.API_KEY
import com.inovasiti.makankini.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.inovasiti.makankini.util.Constants.Companion.QUERY_API_KEY
import com.inovasiti.makankini.util.Constants.Companion.QUERY_DIET
import com.inovasiti.makankini.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.inovasiti.makankini.util.Constants.Companion.QUERY_NUMBER
import com.inovasiti.makankini.util.Constants.Companion.QUERY_TYPE
import com.inovasiti.makankini.util.NetworkResult
import com.inovasiti.makankini.MainViewModel as VM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipe.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private lateinit var viewModel: VM
    private val mAdapter by lazy { RecipeAdapter() }
    private lateinit var mView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(VM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipe, container, false)

        setupRV()
        callAPI()

        return mView
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
        viewModel.getRecipes(applyQueries())
        viewModel.recipesLiveData.observe(viewLifecycleOwner, {
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
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    mView.recyclerview.showShimmer()
                }
            }
        })

    }

    private fun setupRV() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.recyclerview.hideShimmer()
    }

}