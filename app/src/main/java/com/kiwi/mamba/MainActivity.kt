package com.kiwi.mamba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.codelang.library.TrackActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * 进入 track 埋点页面
     */
    fun goPage(view: View) {
        startActivity(Intent(this, TrackActivity::class.java))
    }
}
