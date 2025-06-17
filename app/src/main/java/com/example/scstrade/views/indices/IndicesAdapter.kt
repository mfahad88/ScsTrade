package com.example.scstrade.views.indices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemGroupIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.summary.KSEIndices
import java.util.Collections

class IndicesAdapter(private var itemList: List<KSEIndices>,
                     private val onItemClick: (KSEIndices) -> Unit) : RecyclerView.Adapter<IndicesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGroupIndicesCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kseIndices: KSEIndices) {
            binding.kse100.text = kseIndices.iNDEXCODE.replace("Index","")
            binding.indexValue.text = Utils.convertToMillions(kseIndices.cURRENTINDEX.toDouble())
            binding.indexValue.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,if(kseIndices.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up,0)
//            binding.indexValue.drawable= AppCompatResources.getDrawable(binding.root.context,if(kseIndices.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up)
            binding.labelText.setText(kseIndices.nETCHANGE,kseIndices.preClose.toString())
            binding.volume.text = kseIndices.vOLUMETRADED
            binding.valueTrade.text = kseIndices.vALUETRADED
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
        holder.bind(itemList[position])
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
