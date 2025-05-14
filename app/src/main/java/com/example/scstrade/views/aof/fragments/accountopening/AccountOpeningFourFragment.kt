package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFourBinding
import com.example.scstrade.views.aof.AofActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningFourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningFourFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningFourBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningFourBinding.inflate(inflater,container,false)
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningThreeFragment())
        }
        return binding.root
    }

}