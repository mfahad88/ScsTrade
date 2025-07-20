package com.example.scstrade.views.portfolio.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemTransactionBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import java.util.Date
import java.util.Locale

class TradingLogAdapter(
    private val items: List<PortfolioDetails>,
    private val onItemEdit: (PortfolioDetails,Boolean) -> Unit, private val onItemDelete: (PortfolioDetails) -> Unit
) : RecyclerView.Adapter<TradingLogAdapter.TradingLogViewHolder>() {

    inner class TradingLogViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingLogViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TradingLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TradingLogViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            // Set content
            textDate.text = Utils.convertDateString(item.portfolioDate,"dd-MMM-yyyy")
            textShares.text = "Shares: ${item.portfolioQuantity}"
            textRate.text = "Rate: %.2f".format(item.portfolioRate)
            textCommission.text = "Commission: ${item.portfolioCommission}"
            textPercentage.text = item.portfolioCommissionType

            // BUY / SELL logic
            val isBuy = item.portfolioType.equals("BUY", true)
            buyTag.text = if (isBuy) "BUY" else "SELL"

            // Set chip background tint
            val tagColor = ContextCompat.getColor(root.context,
                if (isBuy) R.color.success_green else R.color.error_red)
//            buyTag.background.setTint(tagColor)
            buyTag.setTextColor(ContextCompat.getColor(root.context, R.color.black))

            // Set CardView background color
            val cardColor = ContextCompat.getColor(root.context,
                if (isBuy) R.color.buy_background else R.color.sell_background)
            root.setCardBackgroundColor(cardColor)
            editIcon.setOnClickListener {
                Utils.showPopup(root.context,editIcon,null, listOf("Edit","Delete")){
                    if(it.contains("Edit",true)){
                        onItemEdit(item,isBuy)
                    }else{
                        onItemDelete(item)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int = items.size

    private fun formatDate(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
            outputFormat.format(inputFormat.parse(input) ?: Date())
        } catch (e: Exception) {
            input
        }
    }
}

