package com.inovasiti.makankini

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.inovasiti.makankini.databinding.FragmentRecipeBinding
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.inovasiti.makankini.util.NetworkResult
import com.inovasiti.makankini.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RecipeFragment : Fragment(), SearchView.OnQueryTextListener {

    // RecipeFragmentArgs will be auto-generated if we add the action arguments inside the NAVIGATION XML
    private val args by navArgs<RecipeFragmentArgs>()

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var recipesViewModel: RecipeViewModel

    private val mAdapter by lazy { RecipeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

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

        setHasOptionsMenu(true)
        setupRV()
        readDatabase()
        binding.recipeFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheetDialogFragment)
        }

        return binding.root
    }

    private fun setupRV() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.showShimmer()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipesLocal.observeOnce(viewLifecycleOwner, { database ->
                //if db not empty OR bottomSheet is not passing anything (means we apply nothing)
                if (database.isNotEmpty() && !args.toBottomSheet) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setRecipe(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    callAPI()
                }
            })
        }
    }

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE


    private fun callAPI() {
        Log.d("RecipesFragment", "API called!")
        viewModel.getRecipes(recipesViewModel.applyQueries())
        viewModel.recipesResponseLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
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

    private fun searchRecipeAPI(searchQuery: String) {
        binding.recyclerview.showShimmer()
        viewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        viewModel.searchRecipesResponseLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val recipe = response.data
                    recipe?.let { mAdapter.setRecipe(recipe) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    readLocalData()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.recyclerview.showShimmer()
                }
            }
        })

    }

    private fun readLocalData() {
        lifecycleScope.launch {
            viewModel.readRecipesLocal.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setRecipe(database[0].foodRecipe)
                }
            })
        }
    }

    private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchRecipeAPI(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // avoid memory leak
    }

}