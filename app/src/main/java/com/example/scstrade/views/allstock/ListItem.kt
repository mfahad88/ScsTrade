package com.example.scstrade.views.allstock

import com.example.scstrade.model.response.stock.StockItem

sealed class ListItem {
    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ITEM = 1
    abstract val viewType: Int
    data class Header(val title: String) : ListItem(){
        override val viewType: Int = VIEW_TYPE_HEADER
    }
    data class Item(val stockItem: StockItem) : ListItem(){
        override val viewType: Int = VIEW_TYPE_ITEM
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }
}