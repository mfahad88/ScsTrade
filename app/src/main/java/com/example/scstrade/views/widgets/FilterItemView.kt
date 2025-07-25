package com.example.scstrade.views.widgets;


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scstrade.R
import com.example.scstrade.databinding.ViewFilterItemBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.FilterValue

/*class FilterItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val _filterLiveData = MutableLiveData(FilterValue())
    val filterLiveData: LiveData<FilterValue> get() = _filterLiveData

    val binding: ViewFilterItemBinding

    init {
//        val inflater = LayoutInflater.from(context)
        binding = ViewFilterItemBinding.inflate(LayoutInflater.from(context), this)

        // Load title from XML attributes
        context.theme.obtainStyledAttributes(attrs, R.styleable.FilterItemView, 0, 0).apply {
            try {
                val title = getString(R.styleable.FilterItemView_filterTitle)
                binding.tvTitle.text = title ?: ""
            } finally {
                recycle()
            }
        }
        *//*val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, R.array.screener_array)
        binding.spinnerOperator.adapter = adapter*//*

        // Spinner listener
        binding.spinnerOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val operator = binding.spinnerOperator.selectedItem.toString()
                if(operator.equals("Greater than equal to",true)){
                    binding.cardMax.visibility = View.GONE
                    binding.cardMin.visibility = View.VISIBLE
                }else if(operator.equals("Less than equal to",true)){
                    binding.cardMax.visibility = View.VISIBLE
                    binding.cardMin.visibility = View.GONE
                }else{
                    binding.cardMax.visibility = View.VISIBLE
                    binding.cardMin.visibility = View.VISIBLE
                }
                _filterLiveData.value = _filterLiveData.value?.copy(operator = operator)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Text change listener
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val min = binding.etMin.text.toString().toDoubleOrNull()
                val max = binding.etMax.text.toString().toDoubleOrNull()
                _filterLiveData.value = _filterLiveData.value?.copy(min = min, max = max)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etMin.addTextChangedListener(watcher)
        binding.etMax.addTextChangedListener(watcher)
    }

    fun setOperators(operators: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, operators)
        binding.spinnerOperator.adapter = adapter
    }

    fun setAverage(avg: Double){
        binding.tvAvg.text = "Avg: ${String.format("%,.2f", avg)}"
    }

    fun setResult(result: Int) {

        binding.tvResult.text = "Result: $result"
    }
}*/
class FilterItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val _filterLiveData = MutableLiveData(FilterValue())
    val filterLiveData: LiveData<FilterValue> get() = _filterLiveData

    val binding: ViewFilterItemBinding

    private val defaultOperators = listOf("Between two values", "Greater than equal to", "Less than equal to")

    init {
        binding = ViewFilterItemBinding.inflate(LayoutInflater.from(context), this)

        // Load title from XML attributes (optional for static use)
        context.theme.obtainStyledAttributes(attrs, R.styleable.FilterItemView, 0, 0).apply {
            try {
                val title = getString(R.styleable.FilterItemView_filterTitle)
                binding.tvTitle.text = title ?: ""
            } finally {
                recycle()
            }
        }

        // Default spinner options
        setOperators(defaultOperators)

        // Handle spinner selection changes
        binding.spinnerOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val operator = binding.spinnerOperator.selectedItem.toString()

                when (operator.lowercase()) {
                    "greater than equal to" -> {
                        binding.cardMin.visibility = View.VISIBLE
                        binding.cardMax.visibility = View.GONE
                    }
                    "less than equal to" -> {
                        binding.cardMin.visibility = View.GONE
                        binding.cardMax.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.cardMin.visibility = View.VISIBLE
                        binding.cardMax.visibility = View.VISIBLE
                    }
                }

                _filterLiveData.value = _filterLiveData.value?.copy(operator = operator)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Listen for min/max input changes
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val min = binding.etMin.text.toString().toDoubleOrNull()
                val max = binding.etMax.text.toString().toDoubleOrNull()
                _filterLiveData.value = _filterLiveData.value?.copy(min = min, max = max)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etMin.addTextChangedListener(watcher)
        binding.etMax.addTextChangedListener(watcher)
    }

    /**
     * Sets the visible title (used for dynamic views)
     */
    fun setFilterTitle(title: String) {
        binding.tvTitle.text = title
    }

    /**
     * Optional: Override the operator list
     */
    fun setOperators(operators: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, operators)
        binding.spinnerOperator.adapter = adapter
    }

    /**
     * Display average for the field
     */
    fun setAverage(avg: Double) {
//        binding.tvAvg.text = "Avg: ${String.format("%,.2f", avg)}"
    }

    /**
     * Display result count for this filter
     */
    fun setResult(result: Int) {
        binding.tvResult.text = "Result: $result"
    }
}