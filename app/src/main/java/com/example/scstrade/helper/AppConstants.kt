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

        val ACCOUNT_OPENING_REFERENCE= "account_opening_reference"

        val BASIC_DATA_UIN_TYPE = "basic_data_uin_type"
        val BASIC_DATA_UIN_NUMBER = "basic_data_uin_number"
        val BASIC_DATA_SALUTATION = "basic_data_salutation"
        val BASIC_DATA_FULL_NIC_NAME = "basic_data_full_nic_name"
        val BASIC_DATA_DOB= "basic_data_dob"
        val BASIC_DATA_MOTHER_MAIDEN_NAME = "basic_data_mother_maiden_name"
        val BASIC_DATA_NATIONALITY = "basic_data_nationality"
        val BASIC_DATA_MARITAL_STATUS = "basic_data_marital_status"
        val BASIC_DATA_RELATIONSHIP = "basic_data_relationship"
        val BASIC_DATA_RELATIONSHIP_NAME = "basic_data_relationship_name"
        val BASIC_DATA_NIC_TYPE = "BASIC_DATA_NIC_TYPE"
        val BASIC_DATA_NIC_EXPIRY = "BASIC_DATA_NIC_EXPIRY"
        val BASIC_DATA_POB_COUNTRY = "BASIC_DATA_POB_COUNTRY"
        val BASIC_DATA_POB_CITY = "BASIC_DATA_POB_CITY"
        val BASIC_DATA_IVR_SERVICE = "BASIC_DATA_IVR_SERVICE"

        val CONTACT_DETAIL_MOBILE_NUMBER="CONTACT_DETAIL_MOBILE_NUMBER"
        val CONTACT_DETAIL_EMAIL_ADDRESS="CONTACT_DETAIL_EMAIL_ADDRESS"
        val CONTACT_DETAIL_MAILING_ADDRESS="CONTACT_DETAIL_MAILING_ADDRESS"
        val CONTACT_DETAIL_MAILING_PROVINCE="CONTACT_DETAIL_MAILING_PROVINCE"
        val CONTACT_DETAIL_MAILING_CITY="CONTACT_DETAIL_MAILING_CITY"
        val CONTACT_DETAIL_OFFICE_NUMBER="CONTACT_DETAIL_OFFICE_NUMBER"
        val CONTACT_DETAIL_RESIDENCE_NUMBER="CONTACT_DETAIL_RESIDENCE_NUMBER"
        val CONTACT_DETAIL_PERMANENT_ADDRESS="CONTACT_DETAIL_PERMANENT_ADDRESS"
        val CONTACT_DETAIL_PERMANENT_COUNTRY="CONTACT_DETAIL_PERMANENT_COUNTRY"


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

        val MARITAL_STATUS= listOf(
            mapOf("Single" to "S"),
            mapOf("Married" to "M")
        )

        val RELATIONSHIP = listOf(
            mapOf("Father" to "F"),
            mapOf("Husband" to "H")
        )

        val SALUTATION = listOf(
            "MR" to "MR",
            "MRS" to "MRS",
            "MS" to "MS"
        )

        val IVRSTATUS = listOf(
            mapOf("Yes" to "Y"),
            mapOf("NO" to "N")
        )

        val LIFETIMECNICSTATUS = listOf(
            mapOf("Life time CNIC is available" to "Y"),
            mapOf("Life Time CNIC is not avilable and expiry date is provided" to "N")
        )

        val IDTYPE = listOf(
            "SNIC - SMART CARD WITH CHIP" to "SNIC",
            "CNIC - CARD WITHOUT CHIP" to "CNIC",
            "NICOP - OVERSEAS PAKISTANI CARD" to "NICOP",
            "POC - PAKISTAN ORIGIN CARD" to "POC"
        )
        val  COUNTRY = listOf(
            "Pakistan" to "PAK",
            "India" to "AS"
        )

        val CITY = listOf(
            "Karachi" to "0001",
            "Bazdar" to "0002"
        )

        val PROVINCE = listOf(
            "OTHER" to "0",
            "FATA / FANA" to "01",
            "SINDH" to "02",
            "PUNJAB" to "03",
            "KHYBER PAKHTUNKHWA" to "04",
            "BALOCHISTAN" to "05",
            "FEDERAL CAPITAL" to "06",
            "A.J.K." to "07"
        )

        val NomineeRelation = listOf(
            "Spouse" to "1",
            "Father" to "2",
            "Mother" to "3",
            "Brother" to "4",
            "Sister" to "5",
            "Daughter" to "6",
            "Son" to "7",
        )
    }
}