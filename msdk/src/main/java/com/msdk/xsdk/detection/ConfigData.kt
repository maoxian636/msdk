package com.msdk.xsdk.detection

import android.app.Activity
import android.content.Intent
import com.msdk.xsdk.ui.XNewActivity

open class ConfigData{

    val RECHARGE:String = "recharge"
    val FIRST_RECHARGE:String = "firstrecharge"
    val CURRENCY:String = "currency"
    val AMOUNT:String = "amount"
    val OPEN_WINDOW:String = "openWindow"


    fun toOtherView(context :Activity,name:String,data:String){
        if (OPEN_WINDOW == name) {
            val intent = Intent(context, XNewActivity::class.java)
            intent.putExtra("data", data)
            context.startActivityForResult(intent, 1)
        }
    }
}
