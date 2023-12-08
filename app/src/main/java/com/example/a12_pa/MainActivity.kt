package com.example.a12_pa

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a12_pa.databinding.ActivityMainBinding
import com.example.a12_pa.model.ItemData
import com.example.a12_pa.recycler.MyAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot


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

        myCheckPermission()
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

    private fun makeRecyclerView() {
        val colRef = MyApplication.db.collection("news")
        val task: Task<QuerySnapshot> = colRef.get()
        task.addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
            override fun onSuccess(result: QuerySnapshot?) {
                val itemList = mutableListOf<ItemData>()
                for (document in result!!) {
                    val item = document.toObject(ItemData::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.mainRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.mainRecyclerView.adapter = MyAdapter(this@MainActivity, itemList)
            }
        })
        task.addOnFailureListener { exception ->
            Log.d("dmu app", "error.. getting document..", exception)
            Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun myCheckPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Toast.makeText(applicationContext, "권한 승인", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_MEDIA_IMAGES//EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)//EXTERNAL_STORAGE)
        }
    }
}