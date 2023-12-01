package com.example.a12_pa.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a12_pa.databinding.ItemMainBinding
import com.example.a12_pa.model.ItemData

class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, private val itemList: MutableList<ItemData>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemMainBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList[position]

        holder.binding.run {
            itemEmailView.text = data.email
            itemDateView.text = data.date
            itemContentView.text = data.content
        }
    }
}

