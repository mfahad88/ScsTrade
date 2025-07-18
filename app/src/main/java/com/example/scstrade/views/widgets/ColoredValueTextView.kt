package com.example.scstrade.views.widgets
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.example.scstrade.R

class ColoredValueTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateColor(s?.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }




    private fun updateColor(text: CharSequence?) {

        if(text?.contains("-")?:false){
            setTextColor(ContextCompat.getColor(context, R.color.md_theme_error))
        }else{
            setTextColor(ContextCompat.getColor(context, R.color.md_theme_primary))
        }

    /*    val value = text?.toString()?.toDoubleOrNull()
        when {
            value == null -> setTextColor(Color.GRAY) // invalid number
            value > 0 -> setTextColor(ContextCompat.getColor(context, R.color.md_theme_primary)) // green
            value < 0 -> setTextColor(ContextCompat.getColor(context, R.color.md_theme_error)) // red
            else -> setTextColor(Color.BLACK) // zero
        }*/
    }
}