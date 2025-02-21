package com.example.scstrade.views.watchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemWatchlistBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.watchList.WatchListItem
import java.util.Collections

class WatchListAdapter(private val itemList: List<WatchListItem>, private val onItemClick: (WatchListItem) -> Unit, private val onItemPopupClick: (String,WatchListItem) -> Unit) : RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    class WatchListViewHolder(private val binding: ItemWatchlistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: WatchListItem,
            onItemClick: (WatchListItem) -> Unit,
            onItemPopupClick: (String,WatchListItem) -> Unit
        ) {

            binding.defaultWat.text = item.watchListMainName
            binding.imageViewThree.setOnClickListener {
                Utils.showPopup(binding.root.context,it,null, listOf("Edit Name","Delete Watchlist") ?: emptyList()){
                    onItemPopupClick(it,item)
                   /* val listType = object : TypeToken<List<LoginDataItem>>() {}
                    val user= Utils.getSharedPreference(binding.root.context, emptyList<LoginDataItem>(),
                        AppConstants.USER,listType)
                   val login=user.first()
                    if(it.equals("Delete Watchlist",true)) {
                        (binding.root.parent as WatchlistFragment).viewModel.deleteWatchList(
                            item.watchListMainID,
                            login.registrationID
                        )
                    }*/
                }
//                onItemClick(item)
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        val binding = ItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick,onItemPopupClick)
    }

    override fun getItemCount(): Int {
        return itemList.size
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
