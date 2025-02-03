package com.example.scstrade.views.indices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemGroupIndicesCardBinding
import com.example.scstrade.model.summary.KSEIndices

class IndicesAdapter(private val itemList: List<KSEIndices>) : RecyclerView.Adapter<IndicesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGroupIndicesCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kseIndices: KSEIndices) {
            binding.kse100.text = kseIndices.iNDEXCODE.replace("Index","")
            binding.indexValue.text = kseIndices.cURRENTINDEX
            binding.labelText.text = kseIndices.nETCHANGE
            binding.volume.text = "Volume: ${kseIndices.vOLUMETRADED}"
            binding.valueTrade.text = "Volume: ${kseIndices.vALUETRADED}"
            binding.high.text = "H: ${kseIndices.hIGHINDEX}"
            binding.l1167000.text = "L: ${kseIndices.lOWINDEX}"
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
}
