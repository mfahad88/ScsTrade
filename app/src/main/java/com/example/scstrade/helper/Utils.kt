package com.example.scstrade.helper

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.app.UiModeManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.scstrade.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Utils {
    companion object{
        private val MY_PREFS="MyPrefs"
        fun helloWorld(){
            print("Hello World!")
        }

        fun commaFormat(value:Double?): String {
            return  NumberFormat.getInstance(Locale.US).format(String.format("%.2f",value).toDouble())
        }
        fun roundTwoDecimal(value:Double?):String{
            if(value!=null) {
                val decimal=value.toString().substringAfter(".","")
                if(decimal.length>2) {
                    return String.format("%,.2f", value)
                }else{
                    if(value>=1000){
                    return String.format("%,.2f", value)
                    }else{
                        return value.toString()
                    }
                }
            }else{
                return (value?:"0.00").toString()
            }
        }
        fun convertToMillions(value: Double?): String {
            val df: DecimalFormat = DecimalFormat("#,###.##")
            if(value!=null){
                if (value >= 1_000_000) {
                    // Convert to millions and append "M"
                    return df.format(value / 1_000_000) + "M"
                } else {
                    return df.format(value)
                }
            }else{
                return "0.0"
            }

        }

        fun animatedValueChange(tv: TextView,from: Double,to:Double,duration: Long = 4000){
            val animator = ValueAnimator.ofFloat(from.toFloat(), to.toFloat()).apply {
                this.duration = duration
                this.setEvaluator { fraction, startValue, endValue ->
                    startValue as Float + (endValue as Float - startValue) * fraction
                }
                addUpdateListener { animation ->
                    val current = (animation.animatedValue as Float).toDouble()
                    tv.setText(roundTwoDecimal(current))
                }
            }
            animator.start()
        }

        fun compareDates(date1:String,date2:String): Boolean {
            val timestamp1 = date1.replace(Regex("[^0-9]"), "").toLong()
            val sdf=SimpleDateFormat("dd/MM/yy",Locale.getDefault())
            val timestamp2 = sdf.parse(date2)?.time
            return if(sdf.format(Date(timestamp1)).compareTo(sdf.format(Date(timestamp2?:0L)))!=0){
                false
            }else {
                true
            }
        }
        fun convertDateString(dateString: String,format: String): String {
            // Extract the timestamp value from the string
            val timestamp = dateString.replace(Regex("[^0-9]"), "").toLong()

            // Convert to Date
            val date = Date(timestamp)

            // Format the date to "dd/MM/yyyy"
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(date)
        }

        fun convertDate(dateString:String): String {

            // Extract the timestamp (milliseconds)
            val timestamp = dateString.substringAfter("(").substringBefore(")").toLong()

            // Convert to Date
            val date = Date(timestamp)

            // Define the output format
            val sdf = SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getDefault() // Set timezone if needed

            return sdf.format(date)
         }
        fun hideKeyboard(context: Context, editText: EditText) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }
        fun setSystemBarIcons(activity: Activity, darkIcons: Boolean) {
            val window =activity.window

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11+ (API 30+)
                val controller = window.insetsController
                controller?.setSystemBarsAppearance(
                    if (darkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                // Android 6+ (API 23+)
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    if (darkIcons) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
            }
        }
        fun convertDateBrFormat(inputDate: String): String {
            return try {
                val inputFormat = SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.ENGLISH)
                val outputFormat = SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.ENGLISH)

                val date: Date = inputFormat.parse(inputDate)!!
                outputFormat.format(date)  // Return formatted date
            } catch (e: Exception) {
                "Invalid Date"  // Handle parsing errors
            }
        }

        fun isDarkMode(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
            } else {
                // For devices below Android 10, check the configuration directly
                (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            }
        }

        fun saveSharedPreference(context: Context,key:String,value:List<Any>){
            val gson=Gson()
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key,gson.toJson(value))
            editor.apply()
        }

        fun <T> getSharedPreference(context: Context, defaultValue: T,key:String,typeToken: TypeToken<T>?=null):T{
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            val json = sharedPreferences.getString(key,null)
            val gson=Gson()

            if(json !=null && typeToken !=null){
               return gson.fromJson(json,typeToken.type)
            }else{
                return defaultValue
            }
        }

        fun removeSharedPrefence(context: Context,key:String){
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
        }

        fun showPopup(context: Context,view: View,menuRes:Int?,items:List<String>?,onItemClick:(String)->Unit ){
            val popupMenu=PopupMenu(context,view)
            if(menuRes!=null){
                popupMenu.menuInflater.inflate(menuRes,popupMenu.menu)
            }else{
                items?.forEach {
                    popupMenu.menu.add(it)
                }
            }
            popupMenu.setOnMenuItemClickListener {
                onItemClick(it.title.toString())
                true
            }
            popupMenu.show()
        }

        fun showInternetError(view: View,message:String,duration:Int=Snackbar.LENGTH_SHORT): Snackbar {
            val snackbar:Snackbar
            if(isDarkMode(view.context)){
                snackbar = Snackbar.make(view, message, duration)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_errorContainer))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )

            }else {
                snackbar= Snackbar.make(view, message, duration)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_error))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )
            }
            return snackbar
        }


        fun showError(view: View,message:String,duration:Int=Snackbar.LENGTH_SHORT): Snackbar {
            val snackbar:Snackbar
            if(isDarkMode(view.context)){
                snackbar = Snackbar.make(view, message, duration)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_errorContainer))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )

            }else {
                snackbar= Snackbar.make(view, message, duration)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_error))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )
            }
            snackbar.show()
            return snackbar
        }

        fun showSuccess(view: View,message:String){
            if(isDarkMode(view.context)){
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_secondaryFixedDim))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )
                    .show()
            }else {
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(view.context, R.color.md_theme_primary))
                    .setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.md_theme_surfaceContainerLowest
                        )
                    )
                    .show()
            }
        }
        fun showConfirmationDialog(context:Context,icon:Int?,title:String?,message:String?,onItemYes:(() -> Unit)? = null): Dialog {
            val dialog=Dialog(context)
            dialog.setContentView(R.layout.dialog_delete)
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_white_container)
            dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            if(icon!=null){
                dialog.findViewById<ImageView>(R.id.imageViewIcon).setImageDrawable(ContextCompat.getDrawable(context,icon))
            }

            if(title!=null){
                dialog.findViewById<TextView>(R.id.title).text = title
            }
            if(message!=null){
                dialog.findViewById<TextView>(R.id.message).text = message
            }
            dialog.findViewById<RelativeLayout>(R.id.buttonNo).setOnClickListener {
                dialog.dismiss()
            }
            dialog.findViewById<RelativeLayout>(R.id.buttonYes).setOnClickListener {
                dialog.dismiss()
                onItemYes?.invoke()
            }
            dialog.show()
            return dialog
        }
        fun showDeleteBottomSheet(context: Context,message: String?, onItemContinue: (() -> Unit)? = null): BottomSheetDialog {
            val bottomSheetDialog=BottomSheetDialog(context)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_deleted)
            if(message!=null){
                bottomSheetDialog.findViewById<TextView>(R.id.your_delete)?.text = message
            }
            bottomSheetDialog.findViewById<RelativeLayout>(R.id.buttonContinue)?.setOnClickListener {
                onItemContinue?.invoke()
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.show()
            return bottomSheetDialog
        }
    }

}