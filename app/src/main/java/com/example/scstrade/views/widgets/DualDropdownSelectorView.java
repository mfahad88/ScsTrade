package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scstrade.R;
import com.example.scstrade.databinding.DualDropdownSelectorViewBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DualDropdownSelectorView extends MaterialCardView {
    private DualDropdownSelectorViewBinding binding;
    public String selectedDropDown1,selectedDropDown2;
    ArrayAdapter adapter1,adapter2;
    public AutoCompleteTextView autoCompleteTextView1,autoCompleteTextView2;
    public EditText textview_1,textview_2;
    public List<Pair<String,String>> entries1,entries2;
    public DualDropdownSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = DualDropdownSelectorViewBinding.inflate(LayoutInflater.from(context),this,true);

        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DualDropdownSelectorView,
                    0,0
            );

            String titleDualDropdown=a.getString(R.styleable.DualDropdownSelectorView_titleDualDropdown);
            int type1= a.getInt(R.styleable.DualDropdownSelectorView_typeDualDropdown1,0);
            int type2= a.getInt(R.styleable.DualDropdownSelectorView_typeDualDropdown2,0);
            String hint1= a.getString(R.styleable.DualDropdownSelectorView_hintDualDropdown1);
            String hint2= a.getString(R.styleable.DualDropdownSelectorView_hintDualDropdown2);
            if(titleDualDropdown!=null){
                binding.title.setText(titleDualDropdown);
                binding.title.setVisibility(View.VISIBLE);
            }else{
                binding.title.setVisibility(View.GONE);
            }

            if(type1==0){
                binding.dropdownMenu1.setVisibility(View.VISIBLE);
                binding.dropdown1.setVisibility(View.GONE);

            }else{
                binding.dropdownMenu1.setVisibility(View.GONE);
                binding.dropdown1.setVisibility(View.VISIBLE);

            }

            if(type2==0){
                binding.dropdownMenu2.setVisibility(View.VISIBLE);
                binding.dropdown2.setVisibility(View.GONE);
            }else{
                binding.dropdownMenu2.setVisibility(View.GONE);
                binding.dropdown2.setVisibility(View.VISIBLE);
            }

            if(hint1!=null) {
                binding.autocompleteTextview1.setHint(hint1);
                binding.autocompleteTextview1.setVisibility(VISIBLE);
            }else{
                binding.autocompleteTextview1.setVisibility(GONE);
                binding.dropdownMenu1.setVisibility(View.GONE);
                binding.dropdown1.setVisibility(View.GONE);
            }
            if(hint2!=null) {
                binding.autocompleteTextview2.setHint(hint2);
                binding.autocompleteTextview2.setVisibility(VISIBLE);
            }else{
                binding.autocompleteTextview2.setVisibility(GONE);
                binding.dropdownMenu2.setVisibility(View.GONE);
                binding.dropdown2.setVisibility(View.GONE);
            }
            if(hint1!=null) {
                binding.textview1.setHint(hint1);
                binding.textview1.setVisibility(View.VISIBLE);
            }else{
                binding.textview1.setVisibility(View.GONE);
            }
            if(hint2!=null) {
                binding.textview2.setHint(hint2);
                binding.textview2.setVisibility(View.VISIBLE);
            }else{
                binding.textview2.setVisibility(View.GONE);
            }

            textview_1 = binding.textview1;
            textview_2 = binding.textview2;
            autoCompleteTextView1=binding.autocompleteTextview1;
            autoCompleteTextView2=binding.autocompleteTextview2;

            binding.autocompleteTextview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    String selectedLabel = (String) parent.getItemAtPosition(position);
                    for (Pair<String, String> item : entries1) {
                        if (item.first.equalsIgnoreCase(selectedLabel)) {
                            selectedDropDown1 = item.second; // e.g. "S"
                            break;
                        }
                    }
                }
            });

            binding.autocompleteTextview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    String selectedLabel = (String) parent.getItemAtPosition(position);
                    for (Pair<String, String> item : entries2) {
                        if (item.first.equalsIgnoreCase(selectedLabel)) {
                            selectedDropDown2 = item.second; // e.g. "S"
                            break;
                        }
                    }
                }
            });

        }
    }

    public void setListEntriesFirst(List<Pair<String,String>> entries){
        this.entries1 = entries;
        List<String> labels= new ArrayList<>();
        for (Pair<String,String> item:entries1){
            labels.add(item.first);
        }
        binding.autocompleteTextview1.setAdapter(new ArrayAdapter(this.binding.getRoot().getContext(),android.R.layout.simple_list_item_1,labels));
    }

    public void setListEntriesSecond(List<Pair<String,String>> entries){
        this.entries2 = entries;
        List<String> labels= new ArrayList<>();
        for (Pair<String,String> item:entries2){
            labels.add(item.first);
        }
        binding.autocompleteTextview2.setAdapter(new ArrayAdapter(this.binding.getRoot().getContext(),android.R.layout.simple_list_item_1,labels));
    }
    public void setList1(List<String> list){
        if(!list.isEmpty()) {
            adapter1 = new ArrayAdapter(this.binding.getRoot().getContext(), android.R.layout.simple_list_item_1, list);
            binding.autocompleteTextview1.setAdapter(adapter1);
        }

    }

    public void setList2(List<String> list){
        if(!list.isEmpty()) {
            adapter2 = new ArrayAdapter(this.binding.getRoot().getContext(), android.R.layout.simple_list_item_1, list);
            binding.autocompleteTextview2.setAdapter(adapter2);
        }

    }


}
