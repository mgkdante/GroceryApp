package com.example.grocery_comparator.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.grocery_comparator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var register: Button
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var userId: String
    private lateinit var userDb: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.appBar)).apply {
            title = "Signing up!"
        }

        auth = Firebase.auth

        this.email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register_button)

        register.setOnClickListener {
            createAccount(email.text.toString() ,password.text.toString())
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    userId = auth.currentUser?.uid.toString()
                    userDb = Firebase.firestore
                    val docData = hashMapOf("Email" to email)
                    val newRef = userDb.collection("Users").document(userId)
                    newRef.set(docData, SetOptions.merge())

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user. Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}