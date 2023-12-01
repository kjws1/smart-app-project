package com.example.a12_pa

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a12_pa.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_save){
            if(binding.addEditView.text.isNotEmpty()){
                saveStore()
            }else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveStore(){
        
        
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }
}