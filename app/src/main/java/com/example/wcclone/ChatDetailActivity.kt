package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.example.wcclone.R
import android.content.Intent
import com.example.wcclone.MainActivity
import com.example.wcclone.Models.MessageMOdel
import com.example.wcclone.Models.ChatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wcclone.databinding.ActivityChatDetailBinding
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*

class ChatDetailActivity : AppCompatActivity() {
    var binding: ActivityChatDetailBinding? = null
    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.hide()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val senderId = auth!!.uid
        val recieveId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profilepic = intent.getStringExtra("profilePic")
        binding!!.username.text = userName
        Picasso.get().load(profilepic).placeholder(R.drawable.avatar3).into(binding!!.progileImage)
        binding!!.backArrow.setOnClickListener {
            val intent = Intent(this@ChatDetailActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val messageModels = ArrayList<MessageMOdel?>()
        val chatAdapter = ChatAdapter(messageModels, this, recieveId)
        binding!!.chatRecyclerView.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(this)
        binding!!.chatRecyclerView.layoutManager = layoutManager
        val senderRoom = senderId + recieveId
        val receiverRoom = recieveId + senderId
        database!!.reference.child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageModels.clear()
                    for (snapshot1 in snapshot.children) {
                        val model = snapshot1.getValue(MessageMOdel::class.java)
                        model!!.messageID = snapshot.key
                        messageModels.add(model)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        binding!!.send.setOnClickListener {
            val message = binding!!.enterMessage.text.toString()
            val model = MessageMOdel(senderId, message)
            model.timestamp = Date().time
            binding!!.enterMessage.setText("")
            database!!.reference.child("chats")
                .child(senderRoom)
                .push()
                .setValue(model).addOnSuccessListener {
                    database!!.reference.child("chats")
                        .child(receiverRoom)
                        .push()
                        .setValue(model).addOnSuccessListener { }
                }
        }
    }
}