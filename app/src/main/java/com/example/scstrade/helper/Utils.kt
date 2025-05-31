package com.example.scstrade.helper

import android.animation.ValueAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.UiModeManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Base64
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Utils {
    companion object{
        private val MY_PREFS="MyPrefs"
        fun helloWorld(){
            print("Hello World!")
        }

        fun commaFormat(value:Double?): String {
            try {
                return NumberFormat.getInstance(Locale.US)
                    .format(String.format("%.2f", value).toDouble())
            }catch (e:Exception){

                e.printStackTrace()
                return ""
            }
        }
        fun roundTwoDecimal(value:Double?):String{
          try{
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
          }catch (e:Exception){

              e.printStackTrace()
              return value.toString()
          }
        }

        fun commaSeparated(value:Int):String{
          try{
              if(value>=1000) {
                  return String.format("%,d", value)
              }else{
                  return value.toString()
              }
          }catch (e:Exception){
              e.printStackTrace()
              return value.toString()
          }
        }
        fun convertToMillions(value: Double?): String {
            try {
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
            }catch (e:Exception){
                e.printStackTrace()
                return "0.0"
            }

        }

        fun animatedValueChange(/*tv: TextView, prefix: String,*/from: Double,to:Double, onUpdate: ((Double) -> Unit)? = null ,duration: Long = 250L){
            val animator = ValueAnimator.ofFloat(from.toFloat(), to.toFloat()).apply {
                this.duration = duration
                this.setEvaluator { fraction, startValue, endValue ->
                    startValue as Float + (endValue as Float - startValue) * fraction
                }
                addUpdateListener { animation ->
                    val current = (animation.animatedValue as Float).toDouble()
                    onUpdate?.invoke(current)
//                    tv.setText("${prefix} ${convertToMillions(current)}")
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

        fun convertIsoToDate(input: String): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val odt = OffsetDateTime.parse(input)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                return odt.format(formatter)
            }else{
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                isoFormat.timeZone = TimeZone.getTimeZone("UTC")

                val date = isoFormat.parse(input)

                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                outputFormat.timeZone = TimeZone.getDefault()

                return outputFormat.format(date!!)
            }
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

        fun getFileNameFromUri(context: Context,uri: Uri): String? {
            var name: String? = null
            val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)

            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    name = cursor.getString(nameIndex)
                }
            }

            return name
        }
        fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun decryptStatus(encryptedStatus: String, encryptionSecret: String = "my32characterlongencryptionsecret!"): String {
            return try {
                // Split the encrypted status into iv and encrypted data parts
                val parts = encryptedStatus.split(":")
                if (parts.size != 2) {
                    throw IllegalArgumentException("Invalid encrypted data format")
                }

                val ivHex = parts[0]
                val encryptedDataHex = parts[1]

                // Convert hex strings to byte arrays
                val iv = hexStringToByteArray(ivHex)
                val encryptedData = hexStringToByteArray(encryptedDataHex)

                // Generate the encryption key using SHA-256 (same as in JS)
                val md = MessageDigest.getInstance("SHA-256")
                val keyBytes = md.digest(encryptionSecret.toByteArray(StandardCharsets.UTF_8))
                val secretKey = SecretKeySpec(keyBytes, "AES")

                // Initialize the cipher for decryption
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val ivSpec = IvParameterSpec(iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

                // Decrypt the data
                val decryptedBytes = cipher.doFinal(encryptedData)

                // Convert decrypted bytes to string
                String(decryptedBytes, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
                "0" // Default to basicdata step on failure
            }
        }


        fun hexStringToByteArray(hexString: String): ByteArray {
            val len = hexString.length
            val data = ByteArray(len / 2)

            var i = 0
            while (i < len) {
                data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) +
                        Character.digit(hexString[i + 1], 16)).toByte()
                i += 2
            }

            return data
        }

        fun generateCaptchaText(length: Int = 6): String {
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
        fun bitmapToBase64(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): String {
            val ratio = Math.min(
                maxWidth.toFloat() / bitmap.width,
                maxHeight.toFloat() / bitmap.height
            )
            val width = (bitmap.width * ratio).toInt()
            val height = (bitmap.height * ratio).toInt()
            return bitmapToBase64(Bitmap.createScaledBitmap(bitmap, width, height, true))
        }
        fun convertImageUriToBase64(context: Context, imageUri: Uri): String? {
            val bitmap = uriToBitmap(context, imageUri)
            return bitmap?.let { resizeBitmap(it,200,200) }
        }
        fun saveSharedPreference(context: Context,key:String,value:List<Any>){
            val gson=Gson()
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key,gson.toJson(value))
            editor.apply()
        }

        fun saveSharedPreference(context: Context,key:String,value:Boolean){
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key,value)
            editor.apply()
        }

        fun  getSharedPreference(context: Context, key:String): Boolean {
            val sharedPreferences=context.getSharedPreferences(MY_PREFS,MODE_PRIVATE)
            return  sharedPreferences.getBoolean(key,true)
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

        fun convertDotNetDateToTimeAgo(dotNetDate: String): String {
            val timestamp = dotNetDate
                .replace("/Date(", "")
                .replace(")/", "")
                .toLongOrNull()

            return if (timestamp != null) {
                DateUtils.getRelativeTimeSpanString(
                    timestamp,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE
                ).toString()
            } else {
                "Invalid date"
            }
        }
        fun showDatePicker(context: Context, onDateSelected: (day: Int, month: Int, year: Int) -> Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                onDateSelected(selectedDay, selectedMonth + 1, selectedYear)
            }, year, month, day)

            datePickerDialog.show()
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
                            R.color.black
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

        fun filterTextField(editText: EditText,regex:Regex){
            editText.filters = arrayOf(object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    return source?.subSequence(start, end)
                        ?.replace(regex, "")
                }
            })
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
            bottomSheetDialog.lifecycleScope.launch {
                delay(5000)
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.show()
            return bottomSheetDialog
        }
    }

}