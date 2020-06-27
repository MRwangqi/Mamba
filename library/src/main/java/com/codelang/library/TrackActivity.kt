package com.codelang.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.codelang.api.annotation.Track

class TrackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
    }

    var open = false

    fun track(view: View) {
        if (open) {
            open()
        } else {
            close()
        }
        open = !open
    }


    private fun open() {
        Toast.makeText(this, "What can I say？Mamba out", Toast.LENGTH_SHORT).show()
    }

    private fun close() {
        Toast.makeText(this, "    Mamba is back", Toast.LENGTH_SHORT).show()
    }



    @Track
    private fun testKotlin(a:Int,b:Float){

    }
}
