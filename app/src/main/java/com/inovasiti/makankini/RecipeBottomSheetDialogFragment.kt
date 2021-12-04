package com.inovasiti.makankini

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_MEAL_TYPE
import kotlinx.android.synthetic.main.fragment_recipe_bottom_sheet_dialog.view.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 * Use the [RecipeBottomSheetDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipeViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_recipe_bottom_sheet_dialog, container, false)

        // when first time load the filter, will read the default value checkedChipId from the mealGroup / dietGroup
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType   //update typeChip
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, mView.mealTypeGroup)   //check chipId & apply typeGroup on UI
            updateChip(value.selectedDietTypeId, mView.dietTypeGroup)
        })


        //set the selected chip in the bottomChip. accept param (ChipGroup group, @IdRes int checkedId)
        mView.mealTypeGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString()
            mealTypeChip = selectedMealType
            mealTypeChipId = checkedId
        }
        mView.dietTypeGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString()
            dietTypeChip = selectedDietType
            dietTypeChipId = checkedId
        }

        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId)
            val action = RecipeBottomSheetDialogFragmentDirections.actionRecipeBottomSheetDialogFragmentToRecipeFragment(true)
            findNavController().navigate(action)
        }
        return mView
    }

    private fun updateChip(id: Int, chipGroup : ChipGroup){
        if(id!=0){
            try{
                chipGroup.findViewById<Chip>(id).isChecked = true
            } catch (e: Exception){
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}