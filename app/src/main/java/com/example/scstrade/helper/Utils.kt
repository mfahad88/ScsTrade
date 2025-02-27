package com.example.scstrade.helper

import android.app.UiModeManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.scstrade.R
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
            return NumberFormat.getInstance(Locale.US).format(value)
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
    }


}