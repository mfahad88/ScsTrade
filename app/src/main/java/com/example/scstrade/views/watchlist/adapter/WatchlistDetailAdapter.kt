package com.example.scstrade.views.watchlist.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemWatchlistDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.views.snapshot.SnapshotActivity
import com.example.scstrade.views.watchlist.WatchListDetailActivity
import java.math.BigDecimal
import java.math.RoundingMode

class WatchListDetailAdapter(
    private val activity: Activity,
    private val onItemClick: (StockItem) -> Unit,
    private val onItemMove: (symbol: String, fromPosition: Int, toPosition: Int) -> Unit
) : ListAdapter<StockItem, WatchListDetailAdapter.WatchListDetailViewHolder>(StockDiffCallback()) {

    private val previousPrices = mutableMapOf<String, Double>()
    private val previousAsk = mutableMapOf<String, Double>()
    private val previousAskVol = mutableMapOf<String, Double>()
    private val previousBid = mutableMapOf<String, Double>()
    private val previousBidVol = mutableMapOf<String, Double>()

    inner class WatchListDetailViewHolder(private val binding: ItemWatchlistDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            stockItem: StockItem,
            previousPrice: Double?,
            previousPriceAsk: Double?,
            previousPriceAskVol: Double?,
            previousPriceBid: Double?,
            previousPriceBidVol: Double?
        ) {
            Glide.with(binding.root.context).load(stockItem.companyLogo)
                .placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.building))
                .circleCrop().into(binding.imageView6)

            binding.shariah.visibility =
                if (stockItem.iN.lowercase().contains("kmi")) View.VISIBLE else View.GONE

            binding.symbol.text = stockItem.sYM
            binding.companyName.text = stockItem.nM
            binding.volume.text = "Vol: ${Utils.convertToMillions(stockItem.v.toDouble())}"
            binding.bidVol.text = Utils.convertToMillions(stockItem.bV.toDouble())
            binding.bid.text = BigDecimal(stockItem.bP).setScale(2, RoundingMode.HALF_UP).toString()
            binding.askVol.text = Utils.convertToMillions(stockItem.aV.toDouble())
            binding.ask.text = BigDecimal(stockItem.aP).setScale(2, RoundingMode.HALF_UP).toString()
            binding.valueTrade.text = String.format("%.2f", stockItem.cL)
            binding.netChange.text =
                "${if (stockItem.cH > 0.0) "+" else ""}${stockItem.cH} ${if (stockItem.cHP > 0.0) "+" else ""}${String.format("%.2f", stockItem.cHP)}%"

            binding.netChange.setTextColor(
                if (stockItem.cH >= 0.0)
                    ContextCompat.getColor(binding.root.context, R.color.md_theme_primary)
                else
                    ContextCompat.getColor(binding.root.context, R.color.md_theme_error)
            )

            binding.high.text = "H: ${BigDecimal(stockItem.hP).setScale(2, RoundingMode.HALF_UP)}"
            binding.low.text = "L: ${BigDecimal(stockItem.lP).setScale(2, RoundingMode.HALF_UP)}"
            binding.high52.text = "H: ${
                if (!TextUtils.isEmpty(stockItem.high52))
                    BigDecimal(stockItem.high52).setScale(2, RoundingMode.HALF_UP)
                else "0.0"
            }"
            binding.low52.text = "L: ${
                if (!TextUtils.isEmpty(stockItem.low52))
                    BigDecimal(stockItem.low52).setScale(2, RoundingMode.HALF_UP)
                else "0.0"
            }"

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, SnapshotActivity::class.java)
                intent.putExtra(AppConstants.SYMBOL, stockItem.sYM)
                binding.root.context.startActivity(intent)
            }

            binding.imageViewThree.setOnClickListener {
                binding.imageViewThree.animate().rotation(180f).setDuration(500).start()
                onItemClick(stockItem)
            }

            animateCard(binding.cardValueTrade, stockItem.cL, previousPrice)
            animateCard(binding.askCard, stockItem.aP, previousPriceAsk)
            animateCard(binding.askVolCard, stockItem.aV.toDouble(), previousPriceAskVol)
            animateCard(binding.bidCard, stockItem.bP, previousPriceBid)
            animateCard(binding.bidVolCard, stockItem.bV.toDouble(), previousPriceBidVol)
        }

        private fun animateCard(view: View, current: Double, previous: Double?) {
            if (previous == null) {
                view.setBackgroundColor(Color.TRANSPARENT)
                return
            }

            val diff = BigDecimal(current).setScale(2, RoundingMode.HALF_UP).toDouble() - previous
            val context = view.context
            when {
                diff > 0 -> view.setBackgroundColor(ContextCompat.getColor(context, R.color.green_increse))
                diff < 0 -> view.setBackgroundColor(ContextCompat.getColor(context, R.color.red_decrease))
                else -> view.setBackgroundColor(Color.TRANSPARENT)
            }

            view.postDelayed({ view.setBackgroundColor(Color.TRANSPARENT) }, 3000)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListDetailViewHolder {
        val binding =
            ItemWatchlistDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListDetailViewHolder, position: Int) {
        val current = getItem(position)
        val previousPrice = previousPrices[current.sYM]
        val previousPriceAsk = previousAsk[current.sYM]
        val previousPriceAskVol = previousAskVol[current.sYM]
        val previousPriceBid = previousBid[current.sYM]
        val previousPriceBidVol = previousBidVol[current.sYM]

        previousPrices[current.sYM] = current.cL
        previousAsk[current.sYM] = current.aP
        previousAskVol[current.sYM] = current.aV.toDouble()
        previousBid[current.sYM] = current.bP
        previousBidVol[current.sYM] = current.bV.toDouble()

        holder.bind(
            current,
            previousPrice,
            previousPriceAsk,
            previousPriceAskVol,
            previousPriceBid,
            previousPriceBidVol
        )
    }

    // ✅ Only rearrange items visually — API called on clearView only
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition || fromPosition !in currentList.indices || toPosition !in currentList.indices) return

        val mutableList = currentList.toMutableList()
        val movedItem = mutableList.removeAt(fromPosition)
        mutableList.add(toPosition, movedItem)

        submitList(mutableList)
    }

    // ✅ Handles drag + triggers API only after drop
    fun getItemTouchHelper(): ItemTouchHelper {
        var fromPosition = -1
        var toPosition = -1

        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                if (from == RecyclerView.NO_POSITION || to == RecyclerView.NO_POSITION) return false

                if (fromPosition == -1) fromPosition = from
                toPosition = to

                swapItems(from, to)
                return true
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                if (fromPosition != -1 && toPosition != -1 && fromPosition != toPosition) {
                    val movedItem = getItem(toPosition)
                    onItemMove(movedItem.sYM, fromPosition, toPosition)
                }

                fromPosition = -1
                toPosition = -1
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // no-op
            }

            override fun isLongPressDragEnabled(): Boolean = true
        })
    }

    class StockDiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.sYM == newItem.sYM
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem == newItem
        }
    }
}
