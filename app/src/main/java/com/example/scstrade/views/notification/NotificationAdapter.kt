package com.example.scstrade.views.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.NotificationItemBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.NotificationEntity
import com.example.scstrade.model.response.notification.NotificationDto
import java.util.Collections

class NotificationAdapter(private val itemList: List<NotificationEntity>, private val onItemClick: (NotificationEntity,Int) -> Unit) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationEntity,index:Int, onItemClick: (NotificationEntity,Int) -> Unit) {
            binding.apply {
                heading.setText(item.title)
                details.setText(item.message)
                time.setText(Utils.convertDotNetDateToTimeAgo(item.timestamp))
                if(item.isRead){
                    imageViewRead.visibility = View.GONE
                }else{
                    imageViewRead.visibility = View.VISIBLE
                }
                imageView28.setOnClickListener {
                    Utils.showPopup(binding.root.context,it,null, listOf("Mark as read")){

                        onItemClick(item,index)
                    }
                }
            }
//            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(itemList[position],position, onItemClick)
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
