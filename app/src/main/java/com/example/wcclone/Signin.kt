package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.app.ProgressDialog
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.content.Intent
import com.example.wcclone.MainActivity
import android.widget.Toast
import com.example.wcclone.Signup
import com.example.wcclone.databinding.ActivitySigninBinding

class Signin : AppCompatActivity() {
    var binding: ActivitySigninBinding? = null
    var mAuth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        supportActionBar!!.hide()
        progressDialog = ProgressDialog(this@Signin)
        progressDialog!!.setTitle("Login")
        progressDialog!!.setMessage("Validating")
        binding!!.button.setOnClickListener {
            if (!binding!!.editTextTextEmailAddress.text.toString()
                    .isEmpty() && !binding!!.editTextTextPassword.toString().isEmpty()
            ) {
                progressDialog!!.show()
                mAuth!!.signInWithEmailAndPassword(
                    binding!!.editTextTextEmailAddress.text.toString(),
                    binding!!.editTextTextPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        progressDialog!!.dismiss()
                        if (task.isSuccessful) {
                            val intent = Intent(this@Signin, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@Signin,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@Signin, "Enter details", Toast.LENGTH_SHORT).show()
            }
        }
        if (mAuth!!.currentUser != null) {
            val intent = Intent(this@Signin, MainActivity::class.java)
            startActivity(intent)
        }
        binding!!.clicktosignup.setOnClickListener {
            val intent = Intent(this@Signin, Signup::class.java)
            startActivity(intent)
        }
    }
}