package com.example.scstrade.views.landing


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemSideMenuBinding

class SideMenuAdapter(
    private var list: Map<Drawable?, String>,
):RecyclerView.Adapter<SideMenuAdapter.MenuViewHolder>() {
//    private val mapEntries: List<Map.Entry<Drawable?, String>> = list.entries.toList()
    inner class  MenuViewHolder(private val binding: ItemSideMenuBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(map: Map.Entry<Drawable?, String>){
            binding.imageView15.setImageDrawable(map.key)
            binding.textView.text = map.value
            System.out.println(map.value)

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=ItemSideMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  MenuViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val map=list.toMap()[position]
        holder.bind(map)
    }


}