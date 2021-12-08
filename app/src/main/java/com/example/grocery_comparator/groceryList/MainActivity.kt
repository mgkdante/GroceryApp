package com.example.grocery_comparator.groceryList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import com.example.grocery_comparator.auth.LoginActivity
import com.example.grocery_comparator.compareLists.ComparePrice
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class MainActivity : AppCompatActivity(), LifecycleOwner {
    private var data: MutableList<ProductItemUI> = ArrayList()
    private var adapter: ListAdapter = ListAdapter(data)
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var buttonLogOut: Button
    private lateinit var userId: String
    private lateinit var compareButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.materialToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        db = Firebase.firestore
        userId = Firebase.auth.uid.toString()

        loadData()

        compareButton = findViewById(R.id.compareButton)
        button = findViewById(R.id.addButton)
        textView = findViewById(R.id.textInput)
        buttonLogOut = findViewById(R.id.button)


        val recyclerView = findViewById<RecyclerView>(R.id.groceryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        buttonLogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        compareButton.setOnClickListener {
            val intent = Intent(this, ComparePrice::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            insertItem()
            val docData1 = hashMapOf("product" to textView.text.toString())
            val newRef1 = db.collection("Users/${userId}/Products").document(textView.text.toString())
                newRef1.set(docData1)
            textView.text = ""
        }
    }

    private fun getData(): Task<QuerySnapshot> {
        return db
            .collection("Users/${userId}/Products")
            .orderBy("product", Query.Direction.DESCENDING)
            .get()
    }

    private fun loadData() {
        getData().addOnCompleteListener {
            if (it.isSuccessful) {
                data = it.result!!.toObjects(ProductItemUI::class.java)
                adapter.dataItems = data
                adapter.notifyDataSetChanged()
            }
            else {
                Log.d("TAG", "Error: ${it.exception!!.message}")
            }
        }
    }

    private fun insertItem(){
        val newItem: ProductItemUI =
            ProductItemUI(
                textView.text.toString()
            )
        data.add(0,newItem)
        adapter.notifyItemInserted(0)
    }

}

