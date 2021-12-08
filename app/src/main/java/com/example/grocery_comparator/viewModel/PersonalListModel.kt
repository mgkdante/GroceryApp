package com.example.grocery_comparator.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocery_comparator.groceryList.ProductItemUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class PersonalListModel: ViewModel() {
    private lateinit var db: FirebaseFirestore
    private var liveData = MutableLiveData<MutableList<ProductItemUI>>()
    private var data: MutableList<ProductItemUI> = ArrayList()
    private lateinit var userId: String


    fun add(item: ProductItemUI){
        data.add(item)
        liveData.value = data
    }

}