package com.example.wcclone

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.wcclone.Adapter.FragementAdapater
import android.view.MenuInflater
import com.example.wcclone.R
import android.widget.Toast
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.example.wcclone.SettingActivity
import com.example.wcclone.GroupChatActivity
import com.example.wcclone.Signin
import com.example.wcclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        mAuth = FirebaseAuth.getInstance()
        binding!!.viewPager.adapter = FragementAdapater(supportFragmentManager)
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                Toast.makeText(this, "Setting is Clicked", Toast.LENGTH_SHORT).show()
                val intent2 = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent2)
            }
            R.id.gchat -> {
                Toast.makeText(this, "Entering GC", Toast.LENGTH_SHORT).show()
                val intent1 = Intent(this@MainActivity, GroupChatActivity::class.java)
                startActivity(intent1)
            }
            R.id.logout -> {
                mAuth!!.signOut()
                val intent = Intent(this@MainActivity, Signin::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}