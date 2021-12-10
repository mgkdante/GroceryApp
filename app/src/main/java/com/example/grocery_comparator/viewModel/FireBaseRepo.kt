package com.example.grocery_comparator.viewModel

import com.example.grocery_comparator.groceryList.ProductItemUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireBaseRepo {
    val TAG = "FIREBASE_REPOSITORY"
    var db= FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    fun saveData(product: ProductItemUI): Task<Void> {
        val newRef1 = db.collection("Users/${user?.uid}/Products").document(product.product)
        return newRef1.set(product)
    }

}