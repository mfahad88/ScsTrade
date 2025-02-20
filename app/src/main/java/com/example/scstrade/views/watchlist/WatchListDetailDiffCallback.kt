package com.example.scstrade.views.watchlist

import androidx.recyclerview.widget.DiffUtil
import com.example.scstrade.model.response.stock.StockItem

class WatchListDetailDiffCallback(
    val oldList:List<StockItem>,
    val newList:List<StockItem>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sYM == newList[newItemPosition].sYM
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}