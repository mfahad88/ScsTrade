package com.example.scstrade.views.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import com.example.scstrade.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.URL

class KSEIndicesWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (appWidgetId in ids) {
            updateWidget(context, manager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://dataapi.scstrade.com/Data?que=KSE%20Indices")
                val response = url.readText()
                val jsonArray = JSONArray(response)

                val widgetView = RemoteViews(context.packageName, R.layout.widget_kse_indices)

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val indexCode = item.getString("INDEX_CODE")
                    val currentIndex = item.getString("CURRENT_INDEX")
                    val netChange = item.getString("NET_CHANGE")

                    val itemView = RemoteViews(context.packageName, R.layout.item_index_widget)
                    itemView.setTextViewText(R.id.tv_index_code, indexCode)
                    itemView.setTextViewText(R.id.tv_index_value, currentIndex)

                    // Set color based on change
                    val changeValue = netChange.toDoubleOrNull() ?: 0.0
                    itemView.setTextViewText(R.id.tv_index_change, netChange)
                    itemView.setTextColor(
                        R.id.tv_index_change,
                        if (changeValue < 0) Color.RED else Color.parseColor("#4CAF50") // green
                    )

                    // Add this item to main widget layout
                    widgetView.addView(R.id.widget_root, itemView)
                }

                manager.updateAppWidget(widgetId, widgetView)

            } catch (e: Exception) {
                Log.e("WidgetError", "Error loading data: ${e.message}")
            }
        }
    }
}
