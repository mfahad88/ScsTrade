package com.example.scstrade.views.allstock

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemHeadingBinding
import com.example.scstrade.databinding.ItemStocksBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.views.snapshot.SnapshotActivity
import com.google.android.material.card.MaterialCardView


class StockAdapter(/*private var list:MutableList<StockItem>,*/var isMore:Boolean=false): /*ListAdapter<ListItem,RecyclerView.ViewHolder>(StockDiffCallback())*/
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var prevList=ArrayList<ListItem>()
    var list=ArrayList<ListItem>()
    inner class StockViewHolder( val binding: ItemStocksBinding):RecyclerView.ViewHolder(binding.root) {
        fun fadeIn(view: View, duration: Long = 500) {
            view.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setListener(object :AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator) {
                            binding.cardValueTrade.postDelayed({
                                binding.cardValueTrade.visibility=View.INVISIBLE
                            },1000)
                            /*  postDelayed({
                                  animate().alpha(0f).setDuration(1000).withEndAction {
                                      postDelayed({
                                          binding.cardValueTrade.visibility=View.INVISIBLE
                                      },100)
                                  }
                              },2000)*/
//                        fadeOut(view,duration)
                        }
                    })
            }
        }

        fun fadeOut(view: View, duration: Long = 300) {
            view.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.cardValueTrade.visibility=View.INVISIBLE
//                    view.visibility = View.INVISIBLE
                    }
                })
        }
        fun bind(previousStockItem: StockItem?,stockItem: StockItem?) {
            if (stockItem!=null){

                binding.root.setOnClickListener {
                    val intent= Intent(binding.root.context, SnapshotActivity::class.java)
                    intent.putExtra(AppConstants.SYMBOL,stockItem.sYM)
                    binding.root.context.startActivity(intent)
                }
                Glide.with(binding.root.context).load(stockItem.companyLogo)
                    .placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.building))
                    .circleCrop()
                    .into(binding.imageView6)
                if(stockItem.iN.lowercase().contains("kmi")){
                    binding.shariah.visibility= View.VISIBLE
                    binding.separator.visibility = View.VISIBLE
                }else{
                    binding.shariah.visibility= View.GONE
                    binding.separator.visibility = View.GONE
                }
                binding.symbol.text = stockItem.sYM
                binding.companyName.text = stockItem.nM
                binding.volume.text = "Vol: ${Utils.convertToMillions(stockItem.v.toDouble())}"


                binding.bidVol.text = "Bid Vol: ${Utils.convertToMillions(stockItem.bV.toDouble())}"
                binding.bid.text = "Bid: ${stockItem.bP}"
                binding.askVol.text = "Ask Vol: ${Utils.convertToMillions(stockItem.aV.toDouble())}"
                binding.ask.text = "Ask: ${stockItem.aP}"

                binding.valueTrade.text = String.format("%.2f",stockItem.cL)


                  val compare = previousStockItem?.cL?:0.0.compareTo(stockItem.cL).toDouble()

                    binding.valueTrade.viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener{
                        override fun onGlobalLayout() {
                            binding.valueTrade.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val width = binding.valueTrade.width
                            val height = binding.valueTrade.height

                            val params=binding.cardValueTrade.layoutParams
                            params.width = width+50
                            params.height = height+10
                            binding.cardValueTrade.layoutParams=params

                            Log.d("TextViewWidth", "Width: $width px")
                        }

                    })
                  if(previousStockItem?.cL==stockItem.cL){
                      binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                  }else if(stockItem.cL>previousStockItem!!.cL){
                      binding.cardValueTrade.setCardBackgroundColor(Color.parseColor("#EDFFE0"))
                    fadeIn(binding.frameValueTrade)

                }else if (stockItem.cL<previousStockItem!!.cL){
                    binding.cardValueTrade.setCardBackgroundColor(Color.parseColor("#FFE0E0"))
                    fadeIn(binding.frameValueTrade)

                }

                /*if(previousStockItem?.cL?.compareTo(stockItem?.cL?:0.0)!=0){
                    Utils.animatedValueChange(previousStockItem?.cL?:0.0,stockItem.cL, onUpdate = {

                    })
                }
*/

                binding.netChange.text = "${if(stockItem.cH<0.0) "" else "+"}${Utils.formatDouble(stockItem.cH)} ${if(stockItem.cH<0.0) "" else "+"}${Utils.formatDouble(stockItem.cHP)}%"
                if(stockItem.cH<0.0){
                    binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context,R.color.md_theme_error))
                }else if(stockItem.cH>0.0){
                    binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context,R.color.md_theme_primary))
                }else{
                    binding.netChange.setTextColor(Color.parseColor("#1A73E8"))
                }

                binding.high.text = "H: ${Utils.formatDouble(stockItem.hP)}"
                binding.low.text = "L: ${Utils.formatDouble(stockItem.lP)}"
                binding.high52.text = if(!TextUtils.isEmpty(stockItem.high52)) stockItem.high52 else "0.0"
                binding.low52.text = if(!TextUtils.isEmpty(stockItem.low52)) stockItem.high52 else "0.0"

                Log.e("Stock:","${previousStockItem.toString()}\n${stockItem.toString()}")

            }
            if(isMore){

                binding.apply {
                    binding.layoutMore.visibility=View.VISIBLE
                    binding.moreDetail.setOnClickListener {
                        if(binding.layoutDetails.visibility==View.GONE){
                            binding.layoutDetails.visibility=View.VISIBLE
                            binding.moreDetail.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_down_icon,0)
                        }else{
                            binding.layoutDetails.visibility=View.GONE
                            binding.moreDetail.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.drop_up_icon,0)
                        }
                    }
                }
            }
        }


    }

    inner class HeadingViewHolder( val binding: ItemHeadingBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String?) {
            if (title!=null){
                binding.leaders.text = title
            }

        }


    }
    override fun getItemViewType(position: Int): Int {

        return list.get(position).viewType
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class StockDiffCallback :  DiffUtil.ItemCallback<ListItem>(){

        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return when {
                oldItem is ListItem.Header && newItem is ListItem.Header ->
                    oldItem.title == newItem.title
                oldItem is ListItem.Item && newItem is ListItem.Item ->
                    oldItem.stockItem.sYM == newItem.stockItem.sYM
                else -> false
            }
//            return oldItem.sYM.equals(newItem.sYM)
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem==newItem
        }

        override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? {
            return if(oldItem is ListItem.Item && newItem is ListItem.Item){
                Bundle().apply {
                    if(oldItem.stockItem.sYM.equals(newItem.stockItem.sYM)){
                        putDouble("oldPrice",oldItem.stockItem.cL)
                        putDouble("newPrice",newItem.stockItem.cL)
                    }
                }.takeIf { it.size()>0 }
            }else null
//            return super.getChangePayload(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ListItem.VIEW_TYPE_HEADER -> {
                val binding=ItemHeadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return  HeadingViewHolder(binding)
            }
            ListItem.VIEW_TYPE_ITEM -> {
                val binding=ItemStocksBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return  StockViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    /*   override fun getItemCount(): Int {
           return list?.size?:0
       }*/
    /* fun submitList(list:List<StockItem>){
         this.list = list
         notifyDataSetChanged()
     }*/

    fun submitList(currentList: List<ListItem>){
        if(list.size==0){
            prevList.addAll(currentList)
        }else {
            prevList.addAll(list)
        }

        list.clear()
        list.addAll(currentList)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(list?.get(position))
        val start = System.nanoTime()
        val item = list.get(position)

        if(item is ListItem.Header){
            (holder as HeadingViewHolder).bind(item.title)
        }else if(item is ListItem.Item){

            val prevItem = prevList.filter { if(it is ListItem.Item){(it as ListItem.Item).stockItem.sYM.equals(item.stockItem.sYM) }else false }.map { it as ListItem.Item }.first()

            (holder as StockViewHolder).bind(prevItem.stockItem,item.stockItem)
        }
        /* when (val item = list.get(position)) {

             is ListItem.Header -> (holder as HeadingViewHolder).bind(item.title)
             is ListItem.Item -> {

                 (holder as StockViewHolder).bind(item.stockItem)
             }

         }*/

        val end = System.nanoTime()
        Log.d("RecyclerView", "Bind time: ${(end - start)/1_000_000} ms")
    }



}