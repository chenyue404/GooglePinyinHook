package com.chenyue404.googlepinyinhook

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class PluginEntry : IXposedHookLoadPackage {
    companion object {
        const val SP_FILE_NAME = "GooglePinyinHook"
        const val SP_KEY = "turn"
        const val TAG = "xposed-GooglePinyin-hook-"

//        fun getPref(): SharedPreferences? {
//            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, SP_FILE_NAME)
//            return if (pref.file.canRead()) pref else null
//        }
    }

    private val PACKAGE_NAME = "com.google.android.inputmethod.pinyin"

    private fun log(str: String) {
        XposedBridge.log(TAG + "\n" + str)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val packageName = lpparam.packageName
        val classLoader = lpparam.classLoader

        if (packageName != PACKAGE_NAME) {
            return
        }

//        XposedHelpers.findAndHookMethod(
//            Window::class.java,
//            "setNavigationBarColor",
//            Int::class.java,
//            object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    log(param.args[0] as String)
//                }
//            }
//        )
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
                    log("com.google.android.apps.inputmethod.libs.framework.core.GoogleInputMethodService#a(com.google.android.apps.inputmethod.libs.framework.core.metadata.KeyboardViewDef.Type)")
                    val color = XposedHelpers.getIntField(param.thisObject, "d")
                    log("color=$color")
                    val inputMethodService = param.thisObject as InputMethodService
                    inputMethodService.window.window?.navigationBarColor =
                        Color.parseColor("#151a15")
                }
            }
        )
    }
}