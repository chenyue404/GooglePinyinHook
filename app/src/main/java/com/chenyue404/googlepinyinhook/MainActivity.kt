package com.chenyue404.googlepinyinhook

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast

/**
 * Created by cy on 2022/1/14.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llRoot = findViewById<LinearLayout>(R.id.llRoot)
        val et0 = findViewById<EditText>(R.id.et0)
        val bt0 = findViewById<ImageButton>(R.id.bt0)

        val sp = getSharedPreferences(PluginEntry.SP_FILE_NAME, Context.MODE_WORLD_READABLE)
        llRoot.setBackgroundColor(sp.getInt(PluginEntry.SP_KEY, Color.TRANSPARENT))
        bt0.setOnClickListener {
            val color = try {
                Color.parseColor("#${et0.text}")
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "错误的颜色", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sp.edit()
                .putInt(PluginEntry.SP_KEY, color)
                .apply()
            llRoot.setBackgroundColor(sp.getInt(PluginEntry.SP_KEY, Color.TRANSPARENT))
        }
    }
}