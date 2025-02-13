package com.example.scstrade.views.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchlistBinding


class WatchlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater,container,false)

        return binding.root
    }



}