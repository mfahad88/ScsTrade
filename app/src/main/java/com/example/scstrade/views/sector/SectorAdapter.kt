package com.example.scstrade.views.sector
import androidx.compose.ui.res.dimensionResource

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemSectorBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import java.util.Collections

class SectorAdapter(private var itemList: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<SectorAdapter.SectorViewHolder>() {

    class SectorViewHolder(private val binding: ItemSectorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onItemClick: (String) -> Unit) {
            binding.textView.text=item
            binding.imageView.setColorFilter(ContextCompat.getColor(binding.root.context,R.color.black))
            if(item.equals("AUTOMOBILE ASSEMBLER",true)){
                binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.automob_ass))

            }
            if(item.equals("AUTOMOBILE PARTS & ACCESSORIES",true)){	binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.	automob_parts))
            }
            if(item.equals("CABLE & ELECTRICAL GOODS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.cable))
            }
            if(item.equals("CEMENT",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.cement))
            }
            if(item.equals("CHEMICAL",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.chemical))
            }
            if(item.equals("COMMERCIAL BANKS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.commercial_bank))
            }
            if(item.equals("ENGINEERING",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.engineering))
            }
            if(item.equals("FERTILIZER",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.fertilizer))
            }
            if(item.equals("FOOD & PERSONAL CARE PRODUCTS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.food))
            }
            if(item.equals("GLASS & CERAMICS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.glass))
            }
            if(item.equals("INSURANCE",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.insurance))
            }
            if(item.equals("INV. BANKS / INV. COS. / SECURITIES COS.",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.inv_bank))
            }
            if(item.equals("JUTE",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.jute))
            }
            if(item.equals("LEASING COMPANIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.leasing))
            }
            if(item.equals("LEATHER & TANNERIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.leather))
            }
            if(item.equals("MISCELLANEOUS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.miscellan))
            }
            if(item.equals("MODARABAS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.modarabas))
            }
            if(item.equals("CLOSE - END MUTUAL FUND",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.mutual_fund))
            }
            if(item.equals("OIL & GAS MARKETING COMPANIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.oilngas))
            }
            if(item.equals("OIL & GAS EXPLORATION COMPANIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.oilngas_exp))
            }
            if(item.equals("PAPER, BOARD & PACKAGING",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.paper_pack))
            }
            if(item.equals("PHARMACEUTICALS",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.pharma))
            }
            if(item.equals("POWER GENERATION & DISTRIBUTION",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.power_gen))
            }
            if(item.equals("PROPERTY",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.property))
            }
            if(item.equals("REAL ESTATE INVESTMENT TRUST",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.realestate))
            }
            if(item.equals("REFINERY",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.refinary))
            }
            if(item.equals("SUGAR & ALLIED INDUSTRIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.sugar))
            }
            if(item.equals("SYNTHETIC & RAYON",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.synthentic))
            }
            if(item.equals("TECHNOLOGY & COMMUNICATION",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.technology))
            }
            if(item.equals("TEXTILE COMPOSITE",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.textile_comp))
            }
            if(item.equals("TEXTILE SPINNING",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.textile_spinning))
            }
            if(item.equals("TEXTILE WEAVING",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.textile_weaving))
            }
            if(item.equals("TOBACCO",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.tobacco))
            }
            if(item.equals("TRANSPORT",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.transport))
            }
            if(item.equals("VANASPATI & ALLIED INDUSTRIES",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.vanaspati))
            }
            if(item.equals("WOOLLEN",true)){binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.woolen))
            }

            /*if(item.equals("AUTOMOBILE PARTS & ACCESSORIES",true)){
                binding.imageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.drawable.automob_parts))
            }*/
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        val binding = ItemSectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        System.out.println(itemList)
        holder.bind(itemList[position], onItemClick)
        /*if(itemList.size==position){
            holder.itemView.findViewById<LinearLayout>(R.id.divider).visibility = View.GONE
        }else{
            holder.itemView.findViewById<LinearLayout>(R.id.divider).visibility = View.VISIBLE
        }*/
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItems(itemList:List<String>){
        this.itemList=itemList
        notifyDataSetChanged()
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
