package com.example.scstrade.views.watchlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchlistBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.factories.WatchListViewModelFactory
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.watchlist.adapter.WatchListAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding
    lateinit var viewModel: WatchListViewModel
    lateinit var login:LoginDataItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(this,
            WatchListViewModelFactory(requireActivity().application,(requireActivity().application as MyApp).viewModel)
        ).get(WatchListViewModel::class.java)
        (parentFragment as LandingFragment).binding.toolbar.binding.apply {
            titleItem.visibility = View.VISIBLE
           group.visibility = View.GONE
            titleItem.text = "Watchlist"
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.setPadding(0,0,0,insets.bottom+250)
            windowInsets
        }

        binding.buttonAdd.setOnClickListener {
            val bottomSheetFragment=AddWatchListBottomSheetFragment()
            val bundle=Bundle()
            bundle.putInt(AppConstants.MODE,0)
            bottomSheetFragment.arguments=bundle
            bottomSheetFragment.show(childFragmentManager,"AddWatchList")
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30.dp))
        }
       fetchUser()


        observeWatchList()
        childFragmentManager.setFragmentResultListener(AppConstants.BOTTOM_SHEET,this){_,bundle->
            val result = bundle.getString(AppConstants.BOTTOM_SHEET_STATUS)
            if(result.equals("done",true)){
                viewModel.getWatchList(login.registrationID?:0)
//                observeWatchList()
            }
        }

        viewModel.mutableDelete.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Error -> Utils.showError(binding.root,it.message?:"Error occurred")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    Utils.showDeleteBottomSheet(requireContext(),"Your watchlist has been deleted.")
//                    viewModel.getWatchList(login.registrationID)
                }
            }
        })

        return binding.root
    }


   public fun observeWatchList(){
       viewModel.mutableWatchListItem.observe(viewLifecycleOwner, Observer { result->
           when(result){
               is Resource.Error -> {
                   Utils.showError(requireView(),result.message?:"")
               }
               is Resource.Loading -> {
                   binding.loader.visibility = View.VISIBLE
                   binding.recyclerView.visibility = View.GONE
               }
               is Resource.Success -> {
                   binding.loader.visibility = View.GONE
                   binding.recyclerView.visibility = View.VISIBLE
                   binding.recyclerView.apply {
                       adapter=
                           WatchListAdapter(result.data?.sortedBy { it.WatchListMainPosition }?.toList()?: emptyList(), onItemClick = { it->
                               val intent = Intent(requireContext(),WatchListDetailActivity::class.java)
                               intent.putExtra(AppConstants.WatchListMainID,it.WatchListMainID)
                               startActivity(intent)

                           }, onItemPopupClick = {str,item->
                               if(str.contains("delete",true)) {
                                   Utils.showConfirmationDialog(requireContext(),null,null,null){
                                       viewModel.deleteWatchList(item.WatchListMainID,login.registrationID?:0)
                                   }
                               }else if(str.contains("edit",true)){
                                   val bottomSheetFragment=AddWatchListBottomSheetFragment()
                                   val bundle=Bundle()
                                   bundle.putInt(AppConstants.MODE,1)
                                   val gson= Gson()
                                   bundle.putString(AppConstants.WATCHLIST_ID,gson.toJson(item))
                                   bottomSheetFragment.arguments=bundle
                                   bottomSheetFragment.show(childFragmentManager,"AddWatchList")
                               }
                           })
                   }
               }
           }
       })
   }

    override fun onResume() {
        viewModel.getWatchList(login.registrationID?:0)
        super.onResume()
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }


}