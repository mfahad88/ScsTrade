package com.example.scstrade.views.landing


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemSideMenuBinding
import com.example.scstrade.model.data.KeyDescValue

class SideMenuAdapter(
    private var list: List<KeyDescValue>,
    private val onItemClick: (KeyDescValue) -> Unit
):RecyclerView.Adapter<SideMenuAdapter.MenuViewHolder>() {
    inner class  MenuViewHolder(private val binding: ItemSideMenuBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(map: KeyDescValue){

            binding.imageView15.setImageDrawable(AppCompatResources.getDrawable(binding.root.context,
                map.value!!
            ))
            binding.textView.text = map.key
            binding.root.setOnClickListener {
                 onItemClick(map)
            }
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
        val map=list[position]

        holder.bind(map)
    }


}