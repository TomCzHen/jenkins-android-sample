package com.example.myfirstapp

import android.content.Intent
import android.icu.text.IDNA
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val secretKeyTextView = findViewById<TextView>(R.id.secretKeyTextView)
        secretKeyTextView.text = BuildConfig.SECRET_KEY

        val versionNameSuffixTextView = findViewById<TextView>(R.id.versionNameSuffixTextView)
        versionNameSuffixTextView.text = getString(R.string.version_name_suffix)
    }

    fun sendMessage() {
        val intent = Intent(this, DisplayMessageActivity::class.java)
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }

    companion object {
        val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
    }
}
