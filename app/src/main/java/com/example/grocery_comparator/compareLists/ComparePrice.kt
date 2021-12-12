package com.example.grocery_comparator.compareLists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import com.example.grocery_comparator.auth.LoginActivity
import com.example.grocery_comparator.groceryList.ProductItemUI
import com.example.grocery_comparator.viewModel.FireBaseRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class ComparePrice : AppCompatActivity() {
    private lateinit var buttonLogOut: Button
    private var firebaseRepo = FireBaseRepo()
    private lateinit var db: FirebaseFirestore
    private var data: MutableList<PricedItemUI> = ArrayList()
    private var groceryList: MutableList<ProductItemUI> = ArrayList()
    private var finalList: MutableList<PricedItemUI> = ArrayList()
    private var exportedData = FireBaseExportedData(data)
    private var customerData = CustomerData(groceryList)
    private var resultListAdapter = ResultListAdapter(finalList)
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_price)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "What you will pay"

        db = firebaseRepo.db
        userId = firebaseRepo.user?.uid.toString()

        buttonLogOut = findViewById(R.id.LogOutButton)


        val recyclerView = findViewById<RecyclerView>(R.id.results)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = resultListAdapter

        loadCustomerData()

        buttonLogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPriceData(): Task<QuerySnapshot> {
        return db
            .collection("grocery_prices")
            .orderBy("item_name", Query.Direction.DESCENDING)
            .get()
    }

    private fun getCustomerData(): Task<QuerySnapshot> {
        return db
            .collection("Users/${userId}/Products")
            .orderBy("product", Query.Direction.DESCENDING)
            .get()
    }

    private fun loadCustomerData() {
        getCustomerData().addOnCompleteListener {
            if (it.isSuccessful) {
                groceryList = it.result!!.toObjects(ProductItemUI::class.java)
                customerData.customerItems = groceryList
                for(item in groceryList){
                    getPriceData().addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            data = task.result!!.toObjects(PricedItemUI::class.java)
                            exportedData.dataItems = data
                            for(product in data){
                                val string = product.item_name
                                if(string.contains(item.product)){
                                  //Log.d("Tag", product.toString())
                                   finalList.add(0, product)
                                    resultListAdapter.dataItem = finalList
                                    resultListAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            } else {
                Log.d("TAG", "Error: ${it.exception!!.message}")
            }
        }
    }
}