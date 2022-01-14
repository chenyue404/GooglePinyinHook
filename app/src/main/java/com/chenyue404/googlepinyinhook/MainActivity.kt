package com.chenyue404.googlepinyinhook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*

/**
 * Created by cy on 2022/1/14.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val v0 = findViewById<View>(R.id.v0)
        val et0 = findViewById<EditText>(R.id.et0)
        val bt0 = findViewById<Button>(R.id.bt0)
        val bt1 = findViewById<Button>(R.id.bt1)

        val sp = getSharedPreferences(PluginEntry.SP_FILE_NAME, Context.MODE_WORLD_READABLE)
        var color = sp.getInt(PluginEntry.SP_KEY, Color.TRANSPARENT)
        v0.setBackgroundColor(color)
        val hexColorStr = colorIntToHex(color)
        et0.text.append(hexColorStr)

        var isColorRight = true

        bt0.setOnClickListener {
            val colorStr = "#${et0.text}"
            color = try {
                isColorRight = true
                Color.parseColor(colorStr)
            } catch (e: IllegalArgumentException) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(et0.windowToken, 0)
                Toast.makeText(this, "错误的颜色", Toast.LENGTH_SHORT).show()
                isColorRight = false
                return@setOnClickListener
            }
            v0.setBackgroundColor(color)
        }
        bt1.setOnClickListener {
            bt0.callOnClick()
            if (!isColorRight) return@setOnClickListener
            sp.edit()
                .putInt(PluginEntry.SP_KEY, color)
                .apply()
            startActivity(
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    data = Uri.parse("package:${PluginEntry.PACKAGE_NAME}")
                })
        }
        v0.setOnClickListener {
            et0.text.apply {
                clear()
                append(colorIntToHex(color))
            }
        }
    }

    private fun colorIntToHex(color: Int): String {
        return String.format("%06X", 0xFFFFFF and color)
    }
}