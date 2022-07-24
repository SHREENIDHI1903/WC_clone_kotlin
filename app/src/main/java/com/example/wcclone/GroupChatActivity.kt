package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.wcclone.MainActivity
import com.google.firebase.database.FirebaseDatabase
import com.example.wcclone.Models.MessageMOdel
import com.google.firebase.auth.FirebaseAuth
import com.example.wcclone.Models.ChatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import com.example.wcclone.databinding.ActivityGroupChatBinding
import java.util.*

class GroupChatActivity : AppCompatActivity() {
    var binding: ActivityGroupChatBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.hide()
        binding!!.backArrow.setOnClickListener {
            val intent = Intent(this@GroupChatActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val database = FirebaseDatabase.getInstance()
        val messageModels = ArrayList<MessageMOdel?>()
        val senderId = FirebaseAuth.getInstance().uid
        binding!!.username.text = "Group Chat"
        val adapter = ChatAdapter(messageModels, this)
        binding!!.chatRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding!!.chatRecyclerView.layoutManager = layoutManager
        database.reference.child("Group Chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageModels.clear()
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue(MessageMOdel::class.java)
                        messageModels.add(model)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        binding!!.send.setOnClickListener {
            val message = binding!!.enterMessage.text.toString()
            val model = MessageMOdel(senderId, message)
            model.timestamp = Date().time
            binding!!.enterMessage.setText("")
            database.reference.child("Group Chat")
                .push()
                .setValue(model)
                .addOnSuccessListener {
                    Toast.makeText(
                        this@GroupChatActivity,
                        "Message Sent",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}