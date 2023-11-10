package com.example.a10_pa

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a10_pa.databinding.ActivityMainBinding
import com.example.a10_pa.model.PageListModel
import com.example.a10_pa.recycler.MyAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val call: Call<PageListModel> = MyApplication.networkService.getList(MyApplication.API_KEY, "kr")
        call.enqueue(object :Callback<PageListModel>{
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.retrofitRecyclerView.adapter = MyAdapter(this@MainActivity, response.body()?.articles)

            }

            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
               Log.d("dmu_app", "error...")
            }
        })
    }
}