package com.example.scstrade.views.watchlist.adapter
import androidx.compose.ui.res.dimensionResource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemSymbolBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import java.util.Collections

class SymbolAdapter(private val itemList: List<StockItem>, private val onItemClick: (StockItem) -> Unit) : RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {
    private var filterList=ArrayList<StockItem>()
    private val selectedItems = mutableSetOf<String>()
    inner class SymbolViewHolder(private val binding: ItemSymbolBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockItem, onItemClick: (StockItem) -> Unit ) {
            binding.symbol.text=item.sYM
            binding.companyName.text = item.nM
            Utils.getCompanyLogo(binding.root.context,binding.imageView6,item)
            if(selectedItems.contains(item.sYM)){
                binding.imageViewSelected.visibility = View.VISIBLE
            }else{
                binding.imageViewSelected.visibility = View.INVISIBLE
            }

            binding.main.setOnClickListener {
                if (selectedItems.contains(item.sYM)) {
                    selectedItems.remove(item.sYM)
                    binding.imageViewSelected.visibility = View.INVISIBLE
                } else {
                    selectedItems.add(item.sYM)
                    binding.imageViewSelected.visibility = View.VISIBLE
                }
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
