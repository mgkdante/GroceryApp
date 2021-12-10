package com.example.grocery_comparator.compareLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import com.example.grocery_comparator.viewModel.FireBaseRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ComparePrice : AppCompatActivity() {
    private var firebaseRepo = FireBaseRepo()
    private lateinit var db: FirebaseFirestore
    private var data: MutableList<PricedItemUI> = ArrayList()
    private var adapter = ResultListAdapter(data)
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_price)
        db = firebaseRepo.db
        userId = firebaseRepo.user?.uid.toString()


        val recyclerView = findViewById<RecyclerView>(R.id.results)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadData()
     }

    private fun getData(): Task<QuerySnapshot> {
        return db
            .collection("grocery_prices")
            .orderBy("item_name", Query.Direction.DESCENDING)
            .get()
    }

    private fun loadData() {
        getData().addOnCompleteListener {
            if (it.isSuccessful) {
                data = it.result!!.toObjects(PricedItemUI::class.java)
                adapter.dataItem = data
                adapter.notifyDataSetChanged()
            }
            else {
                Log.d("TAG", "Error: ${it.exception!!.message}")
            }
        }
    }


}