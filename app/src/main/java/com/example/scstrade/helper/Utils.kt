package com.example.scstrade.helper

import android.app.UiModeManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.Locale

class Utils {
    companion object{
        private val MY_PREFS="MyPrefs"
        fun helloWorld(){
            print("Hello World!")
        }

        fun commaFormat(value:Double): String {
            return NumberFormat.getInstance(Locale.US).format(value)
        }
        fun convertToMillions(value: Double): String {
            val df: DecimalFormat = DecimalFormat("#,###.##")
            return  if (value >= 1_000_000) {
                // Convert to millions and append "M"
                return df.format(value / 1_000_000) + "M"
//                String.format("%.2fm", commaFormat(value / 1_000_000.0))
//                NumberFormat.getInstance(Locale.US).format(million)
            } else {
                // Return the original value if it's less than a million
                    df.format(value)
            }

        }

        fun convertDate(dateString:String): Long {

            if (dateString != null && dateString.startsWith("/Date(") && dateString.endsWith(")/")) {
                val timestamp = dateString.substring(6, dateString.length - 2).toLong()
                return timestamp / (5 * 60 * 1000) * (5 * 60 * 1000)

            }else{
                return  0
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
}