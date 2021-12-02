package com.inovasiti.makankini.util

import androidx.recyclerview.widget.DiffUtil
import com.inovasiti.makankini.model.Result

//DiffUtil.Callback() = calculates the diff between two lists and outputs a list of update operations that converts the first list into the second one
class RecipeResultDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>
): DiffUtil.Callback() {

    //returns old list size
    override fun getOldListSize(): Int {
        return oldList.size
    }

    //returns new list size
    override fun getNewListSize(): Int {
        return newList.size
    }

    //check whether 2 objs are the same item in old & new list
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    //check whether 2 items are the same data
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}