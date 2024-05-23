package com.example.cameraandgalery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cameraandgalery.databinding.ItemRvBinding
import com.example.cameraandgalery.model.Gallery

class GalleryAdapter(var list:ArrayList<Gallery>):RecyclerView.Adapter<GalleryAdapter.Vh>() {
    inner class Vh(var itemRvBinding: ItemRvBinding):ViewHolder(itemRvBinding.root){
        fun onBind(gallery: Gallery, position: Int){
            itemRvBinding.name.text = gallery.name
            itemRvBinding.image.setImageURI(gallery.imageLink.toUri())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }
}