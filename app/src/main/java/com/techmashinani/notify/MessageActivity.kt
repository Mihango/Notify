package com.techmashinani.notify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        Toast.makeText(this, intent.getStringExtra(Intent.EXTRA_TEXT), Toast.LENGTH_LONG).show()
    }
}
