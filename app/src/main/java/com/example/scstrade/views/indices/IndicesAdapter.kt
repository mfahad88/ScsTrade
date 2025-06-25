package com.example.scstrade.views.indices

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemGroupIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.summary.KSEIndices
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Collections

class IndicesAdapter(private var itemList: List<KSEIndices>,
                     private val onItemClick: (KSEIndices) -> Unit) : RecyclerView.Adapter<IndicesAdapter.ViewHolder>() {
    private val previousIndices = mutableMapOf<String, Double>()
    inner class ViewHolder(private val binding: ItemGroupIndicesCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kseIndices: KSEIndices, previousIndex: Double?) {
            binding.kse100.text = kseIndices.iNDEXCODE.replace("Index","")
            binding.indexValue.text = Utils.convertToMillions(kseIndices.cURRENTINDEX.toDouble())
//            binding.indexValue.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,if(kseIndices.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up,0)
//            binding.indexValue.drawable= AppCompatResources.getDrawable(binding.root.context,if(kseIndices.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up)
            binding.labelText.setText(kseIndices.nETCHANGE,kseIndices.preClose.toString())
            binding.volume.text = kseIndices.vOLUMETRADED
            binding.valueTrade.text = kseIndices.vALUETRADED
            if(previousIndex==null){
                binding.cardIndexValue.setCardBackgroundColor(Color.TRANSPARENT)
            }else{
                val diff=
                    BigDecimal(kseIndices.cURRENTINDEX).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousIndex)
                if(diff>0){
                    binding.cardIndexValue.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                }else if (diff<0){
                    binding.cardIndexValue.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                }else{
                    binding.cardIndexValue.setCardBackgroundColor(Color.TRANSPARENT)
                }

                binding.cardIndexValue.postDelayed({
                    binding.cardIndexValue.setCardBackgroundColor(Color.TRANSPARENT)
                },3000)
            }

            if(kseIndices.hIGHINDEX.toDouble().minus(kseIndices.preClose)<0.0) {
                binding.high.text =
                    "H: ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble() ?: 0.0)} ${
                        Utils.formatDouble(
                            kseIndices?.hIGHINDEX?.toDouble()
                                ?.minus(kseIndices?.preClose ?: 0.0) ?: 0.0
                        )
                    } " +
                            "${
                                Utils.formatDouble(
                                    (kseIndices?.hIGHINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100) ?: 0.0
                                )
                            }%"
            }else{
                binding.high.text =
                    "H: ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble() ?: 0.0)} +${
                        Utils.formatDouble(
                            kseIndices?.hIGHINDEX?.toDouble()
                                ?.minus(kseIndices?.preClose ?: 0.0) ?: 0.0
                        )
                    } " +
                            "+${
                                Utils.formatDouble(
                                    (kseIndices?.hIGHINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100) ?: 0.0
                                )
                            }%"
            }
            if(kseIndices.lOWINDEX.toDouble().minus(kseIndices.preClose)<0.0) {
                binding.l1167000.text =
                    "L: ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble() ?: 0.0)} ${
                        Utils.formatDouble(
                            kseIndices?.lOWINDEX?.toDouble()
                                ?.minus(kseIndices?.preClose ?: 0.0) ?: 0.0
                        )
                    } " +
                            "${
                                Utils.formatDouble(
                                    (kseIndices?.lOWINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100) ?: 0.0
                                )
                            }%"
            }else{
                binding.l1167000.text =
                    "L: ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble() ?: 0.0)} +${
                        Utils.formatDouble(
                            kseIndices?.lOWINDEX?.toDouble()
                                ?.minus(kseIndices?.preClose ?: 0.0) ?: 0.0
                        )
                    } " +
                            "+${
                                Utils.formatDouble(
                                    (kseIndices?.lOWINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100) ?: 0.0
                                )
                            }%"
            }
            binding.root.setOnClickListener {
                onItemClick(kseIndices)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupIndicesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        val previousIndex=previousIndices[item.iNDEXCODE]
        holder.bind(item,previousIndex)
        previousIndices[item.iNDEXCODE] = item.cURRENTINDEX.toDouble()
    }
    public fun addItem(itemList: List<KSEIndices>){
        this.itemList = itemList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = itemList.size

    fun swapItems(context: Context, fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}
