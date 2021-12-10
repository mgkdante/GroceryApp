package com.example.grocery_comparator.compareLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import com.example.grocery_comparator.groceryList.ProductItemUI
import com.example.grocery_comparator.viewModel.FireBaseRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ComparePrice : AppCompatActivity() {
    private var firebaseRepo = FireBaseRepo()
    private lateinit var db: FirebaseFirestore
    private var data: MutableList<PricedItemUI> = ArrayList()
    private var finalList: MutableList<PricedItemUI> = ArrayList()
    private var groceryList: MutableList<ProductItemUI> = ArrayList()
    private var exportedData = FireBaseExportedData(data)
    private var customerData = CustomerData(groceryList)
    private var finalData = FinalList(finalList)
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_price)
        db = firebaseRepo.db
        userId = firebaseRepo.user?.uid.toString()

        loadCustomerData()
    }

    private fun getPriceData(): Task<QuerySnapshot> {
        return db
            .collection("grocery_prices")
            .orderBy("item_name", Query.Direction.DESCENDING)
            .get()
    }

  /*  private fun loadPriceData() {
        getPriceData().addOnCompleteListener {
            if (it.isSuccessful) {
                data = it.result!!.toObjects(PricedItemUI::class.java)
                exportedData.dataItems = data

            } else {
                Log.d("TAG", "Error: ${it.exception!!.message}")
            }
        }
    }*/

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
                                  Log.d("Tag", product.toString())
                                   finalList.add(product)
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