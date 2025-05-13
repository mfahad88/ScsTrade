package com.example.scstrade.helper

class AppConstants {
    companion object{
        val STOCK_INFO: String?="stockInfo"
        val PORTFOLIO_MAIN_ID: String?="portfolioMainId"
        val PORTFOLIO_ITEM: String?="portfolioItem"
        val PORTFOLIO_DETAIL= "portfolioDetail"
        val IS_BUY = "isBuy"
        val IS_Sell = "isSell"
        val IS_Dividend = "isDividend"
        val TITLE = "title"
        val SCS: String = "scs"
        val BRECODER: String = "brecoder"
        val TRIBUNE="tribune"
        val PROFIT="profit"
        val METTIS="mettis"
        val DAWN="dawn"
        val NEWS_TYPE: String = "news_type"
        val TECHNICAL_SELECTION: String="technicalSelection"
        val SYMBOL: String="symbol"
        val SYMBOL_STATUS:String = "symbol_status"
        val BOTTOM_SHEET_STATUS: String="bottom_sheet_status"
        val BOTTOM_SHEET: String="bottomsheet"
        val WATCHLIST_ID: String="WatchListId"
        val MODE: String="Mode"    //0 add and 1 update
        val WatchListMainID: String = "watchlist_selected_item"
        val IS_REMEMBER: String="isRemember"
        val USER="user"
        val ACCOUNT_OPENING_FULLNAME="account_opening_fullname"
        val ACCOUNT_OPENING_EMAIL="account_opening_email"
        val ACCOUNT_OPENING_RESIDENTIAL="account_opening_residential"
        val ACCOUNT_OPENING_NIC_TYPE="account_opening_nic_type"
        val ACCOUNT_OPENING_NIC_NUMBER="account_opening_nic_number"

        val NIC_TYPE= listOf(
            mapOf("SNIC - SMART CARD WITH CHIP" to "SNIC"),
            mapOf("CNIC - CARD WITHOUT CHIP" to "CNIC"),
            mapOf("NICOP - OVERSEAS PAKISTANI CARD" to "NICOP"),
            mapOf("POC - PAKISTAN ORIGIN CARD" to "POC")
        )
        val RESIDENTIAL_STATUS= listOf(
            mapOf("Resident" to "01"),
            mapOf("Non-Resident" to "02")
        )
    }
}