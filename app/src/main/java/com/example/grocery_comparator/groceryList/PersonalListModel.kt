package com.example.grocery_comparator.groceryList

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.grocery_comparator.viewModel.FireBaseRepo

class PersonalListModel: ViewModel() {
    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepo = FireBaseRepo()

    fun saveProductToFirebase(product: ProductItemUI) {
        firebaseRepo.saveData(product).addOnFailureListener {
            Log.e(TAG, "Failed to save Product!")
        }
    }


 /*   fun deleteProduct(product: ProductItemUI) {
        firebaseRepo.deleteProduct(product).addOnFailureListener {
            Log.e(TAG, "Failed to delete Address")
        }
    }*/

}
