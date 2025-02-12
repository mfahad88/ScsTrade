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

class IndicesAdapter(private val itemList: List<KSEIndices>,
                     private val onItemClick: (KSEIndices) -> Unit) : RecyclerView.Adapter<IndicesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGroupIndicesCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kseIndices: KSEIndices) {
            binding.kse100.text = kseIndices.iNDEXCODE.replace("Index","")
            binding.indexValue.text = kseIndices.cURRENTINDEX
            binding.indexValue.drawable= AppCompatResources.getDrawable(binding.root.context,if(kseIndices.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up)
            binding.labelText.text = kseIndices.nETCHANGE
            binding.volume.text = "Volume: ${Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble())}"
            binding.valueTrade.text = "Value: ${Utils.convertToMillions(kseIndices.vALUETRADED.toDouble())}"
            binding.high.text = "H: ${kseIndices.hIGHINDEX}"
            binding.l1167000.text = "L: ${kseIndices.lOWINDEX}"
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

    override fun getItemCount(): Int = itemList.size

    fun swapItems(context: Context, fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        Utils.saveSharedPreference(context,"kseIndices",itemList)
        notifyItemMoved(fromPosition, toPosition)
    }
}
