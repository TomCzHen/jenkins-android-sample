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
        val secretKeyTextView = findViewById(R.id.secretKeyTextView) as TextView
        secretKeyTextView.text = BuildConfig.SECRET_KEY

        val versionNameSuffixTextView = findViewById(R.id.versionNameSuffixTextView) as TextView
        versionNameSuffixTextView.text = getString(R.string.version_name_suffix)
    }

    fun sendMessage() {
        val intent = Intent(this, DisplayMessageActivity::class.java)
        val editText = findViewById(R.id.editText) as EditText
        val message = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }

    companion object {
        val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
    }
}
