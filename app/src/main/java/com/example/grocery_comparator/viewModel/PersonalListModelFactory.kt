package com.example.grocery_comparator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class PersonalListModelFactory(): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PersonalListModel::class.java)){
            return PersonalListModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}