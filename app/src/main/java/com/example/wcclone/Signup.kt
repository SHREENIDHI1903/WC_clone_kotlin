package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.app.ProgressDialog
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.example.wcclone.Models.Users
import android.widget.Toast
import android.content.Intent
import com.example.wcclone.Signin
import com.example.wcclone.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    var binding: ActivitySignupBinding? = null
    private var mAuth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        supportActionBar!!.hide()
        progressDialog = ProgressDialog(this@Signup)
        progressDialog!!.setTitle("Creating Account")
        progressDialog!!.setMessage("We're creating your account")
        binding!!.button.setOnClickListener {
            if (!binding!!.username.text.toString()
                    .isEmpty() && !binding!!.editTextTextEmailAddress.text.toString()
                    .isEmpty() && !binding!!.editTextTextPassword.text.toString().isEmpty()
            ) {
                progressDialog!!.show()
                mAuth!!.createUserWithEmailAndPassword(
                    binding!!.editTextTextEmailAddress.text.toString(),
                    binding!!.editTextTextPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        progressDialog!!.dismiss()
                        if (task.isSuccessful) {
                            val user = Users(
                                binding!!.username.text.toString(),
                                binding!!.editTextTextEmailAddress.text.toString(),
                                binding!!.editTextTextPassword.text.toString()
                            )
                            val id = task.result.user!!.uid
                            database!!.reference.child("Users").child(id).setValue(user)
                            Toast.makeText(this@Signup, "SignUp Sucessfull", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this@Signup,
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@Signup, "Enter Creditnails", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.alreadyhaveanaccount.setOnClickListener {
            val intent = Intent(this@Signup, Signin::class.java)
            startActivity(intent)
        }
    }
}