package com.example.a12_pa

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a12_pa.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.addFab.setOnClickListener {
            if (MyApplication.checkAuth()) {
                startActivity(Intent(this, AddActivity::class.java))
            } else {
                Toast.makeText(this, "인증진행해주세요..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // if login was successful
        if (MyApplication.checkAuth()) {
            binding.mainRecyclerView.visibility = View.VISIBLE
            binding.logoutTextView.visibility = View.GONE
        } else {
            binding.mainRecyclerView.visibility = View.GONE
            binding.logoutTextView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, AuthActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}