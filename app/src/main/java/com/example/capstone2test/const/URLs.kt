package com.example.capstone2test.const

object URLs {
    val ROOT_URL:String = "https://capstone-alameri.herokuapp.com/"
    val URL_LOGIN:String = ROOT_URL+"login/"
    /* To update a user add /[id] to URL_USER path */
    val URL_USER:String= ROOT_URL+"user/"
    val URL_USER_SINGLE:String= ROOT_URL+"user/"
    /* user restriction path*/
    val URL_USER_RES:String = ROOT_URL+"res/user_res/"
    // user goals
    val URL_USER_GOALS_G:String = ROOT_URL+"goal/"
    val URL_USER_GOALS_X:String = ROOT_URL+"goal/x"
    //food
    val URL_FOOD:String = ROOT_URL+"food/"
    val URL_FOOD_HISTORY:String = URL_FOOD+"history/"
    //activity
    val URL_ACTIVITY:String = ROOT_URL+"activity/"
    val URL_STATE:String = ROOT_URL+"state/"
    val URL_STATE_RESET:String = URL_STATE+"reset"
    // medication
    val URL_MED:String= ROOT_URL+"med/"
    val URL_MED_HISTORY:String = URL_MED+"record/"
    //External Apis Paths
    val URL_EXAPI_NAME:String = ROOT_URL+"Exapi/name"
    val URL_EXAPI_UPC: String= ROOT_URL+"Exapi/upc"
    val URL_EXAPI_IMG:String = ROOT_URL+"Exapi/image"
}