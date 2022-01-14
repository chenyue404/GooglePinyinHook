package com.chenyue404.googlepinyinhook

import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class PluginEntry : IXposedHookLoadPackage {
    companion object {
        const val SP_FILE_NAME = "GooglePinyinHook"
        const val SP_KEY = "color"
        const val TAG = "xposed-GooglePinyin-hook-"
        const val PACKAGE_NAME = "com.google.android.inputmethod.pinyin"

        fun getPref(): SharedPreferences? {
            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, SP_FILE_NAME)
            return if (pref.file.canRead()) pref else null
        }
    }

    private fun log(str: String) {
        XposedBridge.log(TAG + "\n" + str)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

        XposedHelpers.findAndHookMethod(
            "com.google.android.apps.inputmethod.libs.framework.core.GoogleInputMethodService",
            classLoader,
            "a",
            XposedHelpers.findClass(
                "com.google.android.apps.inputmethod.libs.framework.core.metadata.KeyboardViewDef.Type",
                classLoader
            ),
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
//                    val color = XposedHelpers.getIntField(param.thisObject, "d")
//                    log("color=$color")
                    val color = getPref()?.getInt(SP_KEY, -1) ?: -1
                    log("color=${java.lang.String.format("#%06X", 0xFFFFFF and color)}")
                    val inputMethodService = param.thisObject as InputMethodService
                    if (inputMethodService.window.window?.navigationBarColor != color) {
                        inputMethodService.window.window?.navigationBarColor = color
                    }
                }
            }
        )
    }
}