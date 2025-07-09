package com.example.scstrade.views.watchlist.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemWatchlistDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.views.snapshot.SnapshotActivity
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Collections

class WatchListDetailAdapter(var list:List<StockItem>, val onItemClick: (StockItem) -> Unit) : RecyclerView.Adapter<WatchListDetailAdapter.WatchListDetailViewHolder>() {
    private val previousPrices = mutableMapOf<String, Double>()
    private val previousAsk = mutableMapOf<String, Double>()
    private val previousAskVol = mutableMapOf<String, Double>()
    private val previousBid = mutableMapOf<String, Double>()
    private val previousBidVol = mutableMapOf<String, Double>()
    class WatchListDetailViewHolder(private val binding: ItemWatchlistDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            stockItem: StockItem,
            onItemClick: (StockItem) -> Unit,
            previousPrice: Double?,
            previousPriceAsk: Double?,
            previousPriceAskVol: Double?,
            previousPriceBid: Double?,
            previousPriceBidVol: Double?
        ) {

            Glide.with(binding.root.context).load(stockItem.companyLogo)
                .placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.building))
                .circleCrop()
                .into(binding.imageView6)
            if(stockItem.iN.lowercase().contains("kmi")){
                binding.shariah.visibility= View.VISIBLE
            }else{
                binding.shariah.visibility= View.GONE
            }
            binding.symbol.text = stockItem.sYM
            binding.companyName.text = stockItem.nM
            binding.volume.text = "Vol: ${Utils.convertToMillions(stockItem.v.toDouble())}"
            binding.bidVol.text = "${Utils.convertToMillions(stockItem.bV.toDouble())}"
            binding.bid.text = "${BigDecimal(stockItem.bP).setScale(2, RoundingMode.HALF_UP).toDouble()}"
            binding.askVol.text = "${Utils.convertToMillions(stockItem.aV.toDouble())}"
            binding.ask.text = "${BigDecimal(stockItem.aP).setScale(2, RoundingMode.HALF_UP).toDouble()}"
            binding.valueTrade.text = String.format("%.2f",stockItem.cL)
            binding.netChange.text = "${if (stockItem.cH>0.0) "+" else ""}${stockItem.cH} ${if (stockItem.cHP>0.0) "+" else ""}${String.format("%.2f",stockItem.cHP)}%"
            binding.high.text = "H: ${stockItem.hP.toString()}"
            binding.low.text = "L: ${stockItem.lP.toString()}"
            if(!TextUtils.isEmpty(stockItem.high52)) {
                binding.high52.text = "H: ${stockItem.high52}"
            }else{
                binding.high52.text = "H: ${0.0}"
            }
            if(!TextUtils.isEmpty(stockItem.low52)) {
                binding.low52.text = "L: ${stockItem.low52}"
            }else{
                binding.low52.text = "L: ${0.0}"
            }
            binding.root.setOnClickListener {
                val intent= Intent(binding.root.context, SnapshotActivity::class.java)
                intent.putExtra(AppConstants.SYMBOL,stockItem.sYM)
                binding.root.context.startActivity(intent)
            }
            binding.imageViewThree.setOnClickListener {
                binding.imageViewThree.animate().rotation(180f).setDuration(500).start()
                onItemClick(stockItem)
                /*Utils.showPopup(binding.root.context,it,null, listOf("Delete Company")){

                }*/
            }

            if(previousPrice==null){
                binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                binding.cardValueTrade.alpha=1f
            }else{
                val diff=
                    BigDecimal(stockItem.cL).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPrice)
                if(diff>0){
                    binding.cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.cardValueTrade.postDelayed({
                    binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }


            if(previousPriceAsk==null){
                binding.askCard.setCardBackgroundColor(Color.TRANSPARENT)
                binding.askCard.alpha=1f
            }else{
                val diff=
                    BigDecimal(stockItem.aP).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPriceAsk)
                if(diff>0){
                    binding.askCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.askCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.askCard.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.askCard.postDelayed({
                    binding.askCard.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }

            if(previousPriceAskVol==null){
                binding.askVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                binding.askVolCard.alpha=1f
            }else{
                val diff=
                    BigDecimal(stockItem.aV).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPriceAskVol)
                if(diff>0){
                    binding.askVolCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.askVolCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.askVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.askVolCard.postDelayed({
                    binding.askVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }


            if(previousPriceBid==null){
                binding.bidCard.setCardBackgroundColor(Color.TRANSPARENT)
                binding.bidCard.alpha=1f
            }else{
                val diff=
                    BigDecimal(stockItem.bP).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPriceBid)
                if(diff>0){
                    binding.bidCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.bidCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.bidCard.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.bidCard.postDelayed({
                    binding.bidCard.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }

            if(previousPriceBidVol==null){
                binding.bidVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                binding.bidVolCard.alpha=1f
            }else{
                val diff=
                    BigDecimal(stockItem.bV).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPriceBidVol)
                if(diff>0){
                    binding.bidVolCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.bidVolCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.bidVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.bidVolCard.postDelayed({
                    binding.bidVolCard.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListDetailViewHolder {
        val binding = ItemWatchlistDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListDetailViewHolder, position: Int) {
        val item = list[position]
        val current = item
        val previousPrice = previousPrices[current.sYM]
        val previousPriceAsk= previousAsk[current.sYM]
        val previousPriceAskVol= previousAskVol[current.sYM]
        val previousPriceBid= previousBid[current.sYM]
        val previousPriceBidVol= previousBidVol[current.sYM]
        previousPrices[current.sYM] = current.cL
        previousAsk[current.sYM] = current.aP
        previousAskVol[current.sYM] = current.aV.toDouble()
        previousBid[current.sYM] = current.bP
        previousBidVol[current.sYM] = current.bV.toDouble()
        holder.bind(current, onItemClick,previousPrice,previousPriceAsk,previousPriceAskVol,previousPriceBid,previousPriceBidVol)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun addItems(list: List<StockItem>){
        this.list=list
        notifyDataSetChanged()
    }

    fun swapItems(context: Context, fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                swapItems(viewHolder.itemView.context,fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action needed
            }
        })
    }
}
