package com.example.scstrade.views.watchlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemSymbolBinding
import com.example.scstrade.model.response.stock.StockItem
import java.util.Collections

class SymbolAdapter(private val itemList: List<StockItem>, private val onItemClick: (StockItem) -> Unit) : RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {
    private var filterList=ArrayList<StockItem>()
    class SymbolViewHolder(private val binding: ItemSymbolBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockItem, onItemClick: (StockItem) -> Unit ) {
            binding.symbol.text=item.sYM
            binding.companyName.text = item.nM
//            Glide.with(binding.root.context).load(item.companyLogo).into(binding.imageView6)
            binding.root.setOnClickListener {
                binding.imageViewSelected.visibility = View.VISIBLE
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        val binding = ItemSymbolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SymbolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SymbolViewHolder, position: Int) {
        holder.bind(filterList[position], onItemClick)
    }

    fun filterList(sector:String?,symbol:String?){
        filterList.clear()
        if(sector?.equals("All Sector",true)?:false){
            if(symbol?.isEmpty()?:false){
                filterList.addAll(itemList)
            }else{
                filterList.addAll(itemList.filter { it.sYM.contains(symbol?:"",true) })
            }

        }else{
            if(symbol?.isEmpty()?:false){
                filterList.addAll(itemList.filter { it.sN.contains(sector?:"",true) })
            }else{
                filterList.addAll(itemList.filter { it.sN.contains(sector?:"",true) }.filter { it.sYM.contains(symbol?:"",true) })
            }

        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action needed
            }
        })
    }
}
