package com.example.aniconnect

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    private lateinit var syncBut: Button
    private lateinit var debugText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var lastPkceVerifyString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        syncBut = findViewById(R.id.submit_button_main)
        debugText = findViewById(R.id.debug_text_main)
        progressBar = findViewById(R.id.progressBar_main)

        syncBut.setOnClickListener {
            it.isEnabled = false
            progressBar.visibility = (View.VISIBLE)
            debugText.text = "Retrieving response..."
            login()
        }
        syncBut.isEnabled = true
    }
    private fun login() {
        val veri = PkceUtil.genCodeVerifier(64)
        val url = "https://myanimelist.net/v1/oauth2/authorize" +
                "?response_type=code&client_id=${OAUTH_CLIENT_ID}" +
                "&code_challenge=${veri}&code_challenge_method=plain"
        val i = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
        lastPkceVerifyString = veri
        startActivity(i)
    }
    private fun testQuery(aid: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                val conn = URL("https://myanimelist.net/anime/${aid}").openConnection() as HttpsURLConnection
                val doc = Jsoup.parse(conn.inputStream,null,"https://myanimelist.net/anime/${aid}")
                doc.outerHtml()
            } catch (t: Throwable){
                Log.e(TAG,"Failed getting doc: ${t.message}\n${t.stackTraceToString()}")
                "Failed getting doc: ${t.message}\n${t.stackTraceToString()}"
            }
            runOnUiThread {
                debugText.text = response
                syncBut.isEnabled = true
                progressBar.visibility = (View.INVISIBLE)
            }
        }
    }
    // captures intent from OAuth callback (the only expected after logging in)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        GlobalScope.launch (Dispatchers.IO) {
            // url in the form 'aniconnect://test/?code={authcode}'
            val uri = intent!!.data as Uri
            Log.d(TAG,"URI=${uri}")
            // get the authcode from the url
            val authcode = try {
                uri.getQueryParameter("code")
            } catch (e: Exception) {
                Log.e(TAG,e.stackTraceToString())
                null
            }
            runOnUiThread {
                debugText.text = debugText.text + sb.toString()
            }
            // trade it for an access token for the API
            val accessToken: String
            val tokenURL = URL("https://myanimelist.net/v1/oauth2/token")
            val conn = tokenURL.openConnection() as HttpsURLConnection
            with(conn) {
                try {
                    doOutput = true
                    outputStream.apply {
                        write(("client_id=$OAUTH_CLIENT_ID" +
                                "&grant_type=authorization_code" +
                                "&code=$authcode" +
                                "&code_verifier=$lastPkceVerifyString")
                            .toByteArray(Charsets.US_ASCII))
                    }
                    val om = ObjectMapper()
                    val response: Map<String,String> = om.readValue(inputStream)
                    runOnUiThread {
                        val sb = StringBuilder(debugText.text)
                        sb.append("ACCESS TOKEN GET Success!\nresponse = {\n")
                        for (key in response.keys) sb.append("\t$key : ${response[key]},\n")
                        sb.append("}")
                        debugText.text = sb.toString()
                        syncBut.isEnabled = true
                        progressBar.visibility = View.INVISIBLE
                    }


                } catch (e : Exception) { Log.e(TAG,e.stackTraceToString()) }
                finally { disconnect() }
            }

        }


    }
    companion object {
        const val TAG = "ANIconnect:LoginActivity"
        const val OAUTH_CLIENT_ID = "10479baf97d5d84ef05daf6a02a3cf7b" // registered with MAL
    }
}