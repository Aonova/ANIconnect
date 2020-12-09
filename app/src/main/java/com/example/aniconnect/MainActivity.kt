package com.example.aniconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.io.BufferedInputStream

import java.net.InetAddress
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var submitButton: Button
    private lateinit var responseText: TextView
    private lateinit var inputField: EditText
    private lateinit var loaderView: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submitButton = findViewById(R.id.submit_button)
        responseText = findViewById(R.id.response_text)
        inputField = findViewById(R.id.number_input)
        loaderView = findViewById(R.id.progressBar)

        submitButton.setOnClickListener {
            it.isEnabled = false
            loaderView.visibility = (View.VISIBLE)
            responseText.text = "Retrieving response..."
            testQuery(findViewById<EditText>(R.id.number_input).text.toString().toLong())
        }
        submitButton.isEnabled = true
    }


    private fun testQuery(aid: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                val conn = URL("https://anidb.net/anime/${aid}").openConnection() as HttpsURLConnection
                val doc = Jsoup.parse(conn.inputStream,null,"https://www.android.com/")
                doc.outerHtml()
                } catch (t: Throwable){
                    Log.e(TAG,"Failed getting doc: ${t.message}\n${t.stackTrace}")
                    "Failed getting doc: ${t.message}\n${t.stackTrace}"
                }
            runOnUiThread {
                responseText.text = response
                submitButton.isEnabled = true
                loaderView.visibility = (View.INVISIBLE)
            }
        }
    }

    companion object {
        const val TAG = "ANIconnect:MainActivity"
    }
}