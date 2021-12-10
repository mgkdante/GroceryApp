package com.example.grocery_comparator.groceryList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import com.example.grocery_comparator.auth.LoginActivity
import com.example.grocery_comparator.compareLists.ComparePrice
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var viewModel: PersonalListModel
    private var data: MutableList<ProductItemUI> = ArrayList()
    private lateinit var db: FirebaseFirestore
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var buttonLogOut: Button
    private lateinit var compareButton: FloatingActionButton
    private lateinit var userId: String
    private var adapter = ListAdapter(data)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.materialToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your grocery list"

        viewModel = ViewModelProvider(this)[PersonalListModel::class.java]

        db = viewModel.firebaseRepo.db
        userId = viewModel.firebaseRepo.user?.uid.toString()

        val currentUser = viewModel.firebaseRepo.user
        if(currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        compareButton = findViewById(R.id.compareButton)
        button = findViewById(R.id.addButton)
        textView = findViewById(R.id.textInput)
        buttonLogOut = findViewById(R.id.button)

        val recyclerView = findViewById<RecyclerView>(R.id.groceryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadData()


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
            textView.text = ""
        }
    }


    private fun getData(): Task<QuerySnapshot> {
        return db
            .collection("Users/${userId}/Products")
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

        viewModel.saveProductToFirebase(newItem)
        data.add(0, newItem)
        adapter.notifyItemInserted(0)
    }

}

