package com.example.scstrade.helper

class AppConstants {
    companion object{
        val QTY: String? = "qty"
        val AVG_PRICE: String? = "avg_price"
        val IS_HISTORY: String? = "is_history"
        val PORTFOLIO_NAME: String?= "portfolio_name"
        val LIGHT_MODE: String ="LIGHT_MODE"
        val ID_REF: String="id_ref"
        val ANNOUNCEMENT_TYPE_NAME="announcementTypeName"
        val ACCESS_TOKEN: String? = "access_token"
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
        val ACCOUNT_OPENING_NIC_ISSUE_DATE="account_opening_nic_issue_date"

        val ACCOUNT_OPENING_MOBILE_NUMBER="account_opening_mobile_number"
        val ACCOUNT_OPENING_REGISTERED_UNDER="account_opening_regisered_under"
        val ACCOUNT_OPENING_BANK_IBAN="account_opening_bank_iban"
        val ACCOUNT_OPENING_RELATIVE_NAME="account_opening_relative_name"
        val ACCOUNT_OPENING_RELATIVE_UIN="account_opening_relative_uin"
        val ACCOUNT_OPENING_RELATIONSHIP_TYPE="account_opening_relationship_type"
        val DOCUMENT_IBAN_NAME = "document_iban_name"
        val DOCUMENT_IBAN= "document_iban"

        val DOCUMENT_NIC_FRONT_NAME = "document_nic_front_name"
        val DOCUMENT_NIC_FRONT= "document_nic_front"

        val DOCUMENT_NIC_BACK_NAME = "document_nic_back_name"
        val DOCUMENT_NIC_BACK= "document_nic_back"

        val DOCUMENT_RELATIONSHIP_NAME = "document_nic_relationship_name"
        val DOCUMENT_RELATIONSHIP= "document_relationship"

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


        val NIC_TYPE= listOf(
            ("SNIC - SMART CARD WITH CHIP" to "SNIC"),
            ("CNIC - CARD WITHOUT CHIP" to "CNIC"),
            ("NICOP - OVERSEAS PAKISTANI CARD" to "NICOP"),
            ("POC - PAKISTAN ORIGIN CARD" to "POC")  
        )

        val NIC_TYPE_LIST= listOf(
            "SNIC - SMART CARD WITH CHIP" to "SNIC",
            "CNIC - CARD WITHOUT CHIP" to "CNIC",
            "NICOP - OVERSEAS PAKISTANI CARD" to "NICOP",
            "POC - PAKISTAN ORIGIN CARD" to "POC"
        )
   /*     val RESIDENTIAL_STATUS= listOf(
            mapOf("Resident" to "01"),
            mapOf("Non-Resident" to "02")
        )*/


        val RELATIVE_RELATION= listOf(
            ("MY NAME" to "1"),
            ("MY FATHER'S NAME" to "2"),
            ("MY FATHER'S NAME" to "3"),
            ("MY SON'S NAME" to "4"),
            ("MY DAUGHTER'S NAME" to "5"),
            ("MY HUSBAND'S NAME" to "6"),
            ("MY COMPANY'S NAME" to "7")
        )

        val BANK_SWIFT_CODES= listOf(
            ("ABPA" to "ABPA"),
            ("AIIN" to "AIIN"),
            ("ALFH" to "ALFH"),
            ("APNA" to "APNA"),
            ("ASCM" to "ASCM"),
            ("BAHL" to "BAHL"),
            ("BKIP" to "BKIP"),
            ("BMLP" to "BMLP"),
            ("BPUN" to "BPUN"),
            ("BURJ" to "BURJ"),
            ("CITI" to "CITI"),
            ("DEUT" to "DEUT"),
            ("DUIB" to "DUIB"),
            ("FAYS" to "FAYS"),
            ("FINC" to "FINC"),
            ("FMFB" to "FMFB"),
            ("FWOM" to "FWOM"),
            ("HABB" to "HABB"),
            ("ICBK" to "ICBK"),
            ("JSBL" to "JSBL"),
            ("KHBL" to "KHBL"),
            ("KHYB" to "KHYB"),
            ("MCIB" to "MCIB"),
            ("MEZN" to "MEZN"),
            ("MMBL" to "MMBL"),
            ("MPBL" to "MPBL"),
            ("MUCB" to "MUCB"),
            ("NAYA" to "NAYA"),
            ("NBPA" to "NBPA"),
            ("NIBP" to "NIBP"),
            ("NRSP" to "NRSP"),
            ("PLCO" to "PLCO"),
            ("SADA" to "SADA"),
            ("SAMB" to "SAMB"),
            ("SAUD" to "SAUD"),
            ("SBPP" to "SBPP"),
            ("SCBL" to "SCBL"),
            ("SIND" to "SIND"),
            ("SONE" to "SONE"),
            ("SUMB" to "SUMB"),
            ("TMFB" to "TMFB"),
            ("UMBL" to "UMBL"),
            ("UNIL" to "UNIL"),
            ("ZTBL" to "ZTBL")

        )

        val MARITAL_STATUS= listOf(
            mapOf("Single" to "S"),
            mapOf("Married" to "M")
        )

        val MARITAL_STATUS_LIST= listOf(
            "Single" to "S",
           "Married" to "M"
        )

        val RELATIONSHIP = listOf(
            mapOf("Father" to "F"),
            mapOf("Husband" to "H")
        )
        val RELATIONSHIP_LIST = listOf(
            "Father" to "F",
            "Husband" to "H"
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

        val IVRSTATUSLIST = listOf(
            "Yes" to "Y",
            "NO" to "N"
        )

        val LIFETIMECNICSTATUS = listOf(
            mapOf("Life time CNIC is available" to "Y"),
            mapOf("Life Time CNIC is not avilable and expiry date is provided" to "N")
        )
        val LIFETIMECNICSTATUSLIST = listOf(
            "Yes" to "Y",
            "No" to "N"
        )

        val IDTYPE = listOf(
            "SNIC - SMART CARD WITH CHIP" to "SNIC",
            "CNIC - CARD WITHOUT CHIP" to "CNIC",
            "NICOP - OVERSEAS PAKISTANI CARD" to "NICOP",
            "POC - PAKISTAN ORIGIN CARD" to "POC"
        )
        var  COUNTRY = listOf(
            "Pakistan" to "PAK",
            "India" to "AS"
        )

        var CITY = listOf(
            "Karachi" to "0001" to "",
            "Bazdar" to "0002" to ""
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

       val AnnualIncomeSahulat = listOf(
            "UP TO 100,000" to "J07",
            "100,001 - 250,000" to "J08",
            "250,001 - 500,000" to "J09",
            "Above 500,000" to "J10",
       )

                val AnnualIncomeNormal = listOf(
            "UP TO 100,000" to "J01",
            "100,001 - 250,000" to "J02",
            "250,001 - 500,000" to "J03",
            "500,001 - 1,000,000" to "J04",
            "1,000,001 - 2,500,000" to "J05",
            "Above 2,500,000" to "J06"
        )

        val AccountType = listOf(
            "Normal Account" to "NKA",
            "Sahulat Account" to "SKA",
        )

        val Occupation = listOf(
            "Agriculturist" to "P001",
            "Business" to "P002",
            "Business Executive" to "P003",
            "House Hold" to "P005",
            "House Wife" to "P006",
            "Industrialist" to "P007",
            "Professional" to "P012",
            "Retired Person" to "P013",
            "Service" to "P014",
            "Student" to "P015",
            "Govt. / Public Sector" to "P019",
            "Others" to "P999",
        )

        val RESIDENTIALSTATUS = listOf(
            "RESIDENT" to "01",
            "NON-RESIDENT" to "02",
        )

        val ZakatType = listOf(
            "Muslim Zakat Deductible" to "5",
            "Muslim Zakat Non-Deductible" to "6",
            "Not Applicable" to "7",
        )

        val ATTORNEYTYPE = listOf(
            "SELF" to "S",
            "OTHER" to "O"
        )
        
        val NomineeType = listOf(
            "Yes" to "Y",
            "NO" to "N",
        )
        
        val RemittanceDescription = listOf(
            "Non-Resid`ent Pakistani (Repatriable)" to "1",
            "Non-Resident Pakistani (Non-Repatriable)" to "2",
            "Foreigner Resident (Repatriable)" to "3",
            "Foreigner Resident (Non-Repatriable)" to "4",
            "Foreigner Non-Resident (Repatriable)" to "5",
            "Foreigner Non-Resident (Non-Repatriable)" to "6",
            "Resident Pakistani" to "7"
            )

        val LifeCycleStatus = listOf(
            "BASIC_DATA" to 70,
            "CONTACT_DETAILS" to 80,
            "ATTORNEY_DETAILS" to 90,
            "NOMINEE_DETAILS" to 100,
            "OTHER_DETAILS" to 110,
            "DOCUMENTS" to 120,
            "COMPLETED" to 140
        )
    }
}