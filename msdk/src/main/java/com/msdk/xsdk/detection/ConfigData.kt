package com.msdk.xsdk.detection

import android.app.Activity
import android.content.Intent
import com.msdk.xsdk.ui.XNewActivity
import com.msdk.xsdk.utils.XAES

open class ConfigData{

    val RECHARGE:String = "At/E928i9g/QSzvKiMDMrg=="
    val FIRST_RECHARGE:String = "6Uaa1xn5NsUjHjTk22vSLg=="
    val CURRENCY:String = "OuZIU+NWDDUFaNCAMskadw=="
    val AMOUNT:String = "hKf/hc0zMXyyjO8i77D26A=="
    val OPEN_WINDOW:String = "khN9nYZt0DDG80K4DWl20w=="


    fun toOtherView(context :Activity,name:String,data:String){
        if (XAES.B2SrclYHQH3aCQJL(OPEN_WINDOW) == name) {
            val intent = Intent(context, XNewActivity::class.java)
            intent.putExtra("data", data)
            context.startActivityForResult(intent, 1)
        }
    }
}
