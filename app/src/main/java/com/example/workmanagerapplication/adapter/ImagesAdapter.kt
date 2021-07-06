package com.example.workmanagerapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.workmanagerapplication.R
import com.example.workmanagerapplication.model.RecyclerItemModel
import com.example.workmanagerapplication.databinding.ItemImageBinding

class ImagesAdapter(var list: ArrayList<RecyclerItemModel>) :
    RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    class ImagesViewHolder(var view: ItemImageBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemImageBinding>(
            inflater,
            R.layout.item_image,
            parent,
            false
        )
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.view.image = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<RecyclerItemModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}