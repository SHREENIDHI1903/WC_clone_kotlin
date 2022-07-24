package com.example.wcclone.Fragments

import com.example.wcclone.Models.Users
import com.google.firebase.database.FirebaseDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.wcclone.Adapter.UsersAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wcclone.databinding.FragmentChatsBinding
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class ChatsFragment : Fragment() {
    var binding: FragmentChatsBinding? = null
    var list = ArrayList<Users?>()
    var database: FirebaseDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()
        val adapter = UsersAdapter(list, requireContext())
        binding!!.chatRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        binding!!.chatRecyclerView.layoutManager = layoutManager
        database!!.reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val users = dataSnapshot.getValue(Users::class.java)
                    users!!.userId = dataSnapshot.key
                    //to not keep urself in chat
                    if (users.userId != FirebaseAuth.getInstance().uid) {
                        list.add(users)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return binding!!.root
    }
}