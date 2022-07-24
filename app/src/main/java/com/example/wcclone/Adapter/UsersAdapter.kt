package com.example.wcclone.Adapter

import android.content.Context
import com.example.wcclone.Models.Users
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.wcclone.R
import com.squareup.picasso.Picasso
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.example.wcclone.ChatDetailActivity
import android.widget.TextView
import java.util.ArrayList

class UsersAdapter(var list: ArrayList<Users?>, var context: Context) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = list[position]
        Picasso.get().load(users.profilePic).placeholder(R.drawable.avatar3).into(holder.image)
        holder.userName.text = users.userName
        //set last message
        FirebaseDatabase.getInstance().reference.child("chats")
            .child(FirebaseAuth.getInstance().uid + users.userId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (snapshot1 in snapshot.children) {
                            holder.lastmessage.text = snapshot1.child("message").value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra("userId", users.userId)
            intent.putExtra("profilePic", users.profilePic)
            intent.putExtra("userName", users.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var userName: TextView
        var lastmessage: TextView

        init {
            image = itemView.findViewById(R.id.profilepic)
            userName = itemView.findViewById(R.id.usernamelist)
            lastmessage = itemView.findViewById(R.id.lastmessage)
        }
    }
}