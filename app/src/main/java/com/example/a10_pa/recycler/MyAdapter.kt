package com.example.a10_pa.recycler

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a10_pa.databinding.ItemMainBinding
import com.example.a10_pa.model.ItemModel

class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val datas: MutableList<ItemModel>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    //override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    //    return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    //}

    // Creates each view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding

        //add......................................
        val model = datas!![position]
        binding.itemTitle.text = model.title
        // No.2 Makes clicking title go to article page
        binding.itemTitle.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.url))
            holder.itemView.context.startActivity(intent)
        }
        binding.itemDesc.text = model.description
        // No.1 Shows source name if author is null
        if (model.author == null) {
            model.author = model.source?.name
        }
        binding.itemTime.text = "${model.author} At ${model.publishedAt}"

        Glide.with(context)
            .load(model.urlToImage)
            .into(binding.itemImage)
    }
}