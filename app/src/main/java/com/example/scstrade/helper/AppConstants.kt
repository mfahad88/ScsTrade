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

        val ACCOUNT_OPENING_MOBILE_NUMBER="account_opening_mobile_number"
        val ACCOUNT_OPENING_REGISTERED_UNDER="account_opening_regisered_under"
        val ACCOUNT_OPENING_BANK_IBAN="account_opening_bank_iban"
        val DOCUMENT_IBAN_NAME = "document_iban_name"
        val DOCUMENT_IBAN= "document_iban"

        val DOCUMENT_NIC_FRONT_NAME = "document_nic_front_name"
        val DOCUMENT_NIC_FRONT= "document_nic_front"

        val DOCUMENT_NIC_BACK_NAME = "document_nic_back_name"
        val DOCUMENT_NIC_BACK= "document_nic_back"

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

        val RELATIVE_RELATION= listOf(
            mapOf("MY NAME" to "1"),
            mapOf("MY FATHER'S NAME" to "2"),
            mapOf("MY FATHER'S NAME" to "3"),
            mapOf("MY SON'S NAME" to "4"),
            mapOf("MY DAUGHTER'S NAME" to "5"),
            mapOf("MY HUSBAND'S NAME" to "6"),
            mapOf("MY COMPANY'S NAME" to "7")
        )

        val BANK_SWIFT_CODES= listOf(
            mapOf("ABPA" to "ABPA"),
            mapOf("AIIN" to "AIIN"),
            mapOf("ALFH" to "ALFH"),
            mapOf("APNA" to "APNA"),
            mapOf("ASCM" to "ASCM"),
            mapOf("BAHL" to "BAHL"),
            mapOf("BKIP" to "BKIP"),
            mapOf("BMLP" to "BMLP"),
            mapOf("BPUN" to "BPUN"),
            mapOf("BURJ" to "BURJ"),
            mapOf("CITI" to "CITI"),
            mapOf("DEUT" to "DEUT"),
            mapOf("DUIB" to "DUIB"),
            mapOf("FAYS" to "FAYS"),
            mapOf("FINC" to "FINC"),
            mapOf("FMFB" to "FMFB"),
            mapOf("FWOM" to "FWOM"),
            mapOf("HABB" to "HABB"),
            mapOf("ICBK" to "ICBK"),
            mapOf("JSBL" to "JSBL"),
            mapOf("KHBL" to "KHBL"),
            mapOf("KHYB" to "KHYB"),
            mapOf("MCIB" to "MCIB"),
            mapOf("MEZN" to "MEZN"),
            mapOf("MMBL" to "MMBL"),
            mapOf("MPBL" to "MPBL"),
            mapOf("MUCB" to "MUCB"),
            mapOf("NAYA" to "NAYA"),
            mapOf("NBPA" to "NBPA"),
            mapOf("NIBP" to "NIBP"),
            mapOf("NRSP" to "NRSP"),
            mapOf("PLCO" to "PLCO"),
            mapOf("SADA" to "SADA"),
            mapOf("SAMB" to "SAMB"),
            mapOf("SAUD" to "SAUD"),
            mapOf("SBPP" to "SBPP"),
            mapOf("SCBL" to "SCBL"),
            mapOf("SIND" to "SIND"),
            mapOf("SONE" to "SONE"),
            mapOf("SUMB" to "SUMB"),
            mapOf("TMFB" to "TMFB"),
            mapOf("UMBL" to "UMBL"),
            mapOf("UNIL" to "UNIL"),
            mapOf("ZTBL" to "ZTBL")

        )
    }
}