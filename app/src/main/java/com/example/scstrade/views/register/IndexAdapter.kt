package com.example.scstrade.views.register

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.summary.KSEIndices

class IndexAdapter(private val list:List<KSEIndices>):RecyclerView.Adapter<IndexAdapter.IndexViewHolder>() {

    inner class  IndexViewHolder(private val binding: ItemIndicesCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(kseIndices: KSEIndices){
            binding.apply {

                kse100.text = kseIndices.iNDEXCODE
                tradingValue.text = Utils.convertToMillions(kseIndices.vALUETRADED.toDouble())
                netChange.text = kseIndices.nETCHANGE
                volume.text = Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble())

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder {
        val binding=ItemIndicesCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  IndexViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        val kseIndices=list[position]
        holder.bind(kseIndices)
    }

}