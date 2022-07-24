package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.os.Bundle
import android.content.Intent
import com.example.wcclone.MainActivity
import android.widget.Toast
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.example.wcclone.Models.Users
import com.squareup.picasso.Picasso
import com.example.wcclone.R
import com.example.wcclone.databinding.ActivitySettingBinding
import com.google.firebase.database.DatabaseError
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnSuccessListener
import java.util.HashMap

class SettingActivity : AppCompatActivity() {
    var binding: ActivitySettingBinding? = null
    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.hide()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding!!.backArrow.setOnClickListener {
            val intent = Intent(this@SettingActivity, MainActivity::class.java)
            startActivity(intent)
        }
        binding!!.saveButton.setOnClickListener {
            if (binding!!.etStatus.text.toString() != "" && binding!!.txtUsername.text.toString() != "") {
                val status = binding!!.etStatus.text.toString()
                val username = binding!!.txtUsername.text.toString()
                val obj = HashMap<String, Any>()
                obj["userName"] = username
                obj["status"] = status
                database!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
                    .updateChildren(obj)
            } else {
                Toast.makeText(this@SettingActivity, "Please enter details", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        database!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(Users::class.java)
                    Picasso.get()
                        .load(users!!.profilePic)
                        .placeholder(R.drawable.img_1)
                        .into(binding!!.profileImg)
                    binding!!.etStatus.setText(users.status)
                    binding!!.txtUsername.setText(users.userName)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        binding!!.plus.setOnClickListener {
            val intent1 = Intent()
            intent1.action = Intent.ACTION_GET_CONTENT
            intent1.type = "image/*"
            startActivityForResult(intent1, 25)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!!.data != null) {
            val sFile = data.data
            binding!!.profileImg.setImageURI(sFile)
            val reference = storage!!.reference.child("profile_img")
                .child(FirebaseAuth.getInstance().uid!!)
            reference.putFile(sFile!!).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    database!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
                        .child("profilePic").setValue(uri.toString())
                }
            }
        }
    }
}