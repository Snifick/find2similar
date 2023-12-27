package com.find.twosim.illar.trr

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.find.twosim.illar.trr.ui.Game
import com.find.twosim.illar.trr.ui.Load
import com.find.twosim.illar.trr.ui.theme.Find2SimilarTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scopik = CoroutineScope(Dispatchers.IO)
        setContent {
            Find2SimilarTheme {
                val weAreGood = remember {
                    mutableStateOf(false)
                }
                val navController = rememberNavController()
                val cheeper = remember {
                    OkHttpClient()
                }
                LaunchedEffect(key1 = Unit, block = {
                    scopik.launch {
                        try {
                            delay(100)
                            val ipcheck = Request.Builder().url("https://api.ipify.org").build()
                            val ipresp = cheeper.newCall(ipcheck).execute()
                            val ip = ipresp.body()!!.string().toString()
                            val req = Request.Builder().url("http://ip-api.com/json/$ip").build()
                            val resp = cheeper.newCall(req).execute()

                            if (resp.code() == 200) {
                                val jsonchik = resp.body()!!.string().toString()
                                val country = JSONObject(jsonchik).get("countryCode")
                                if (country.equals("RU")) {
                                    weAreGood.value = true
                                    withContext(Dispatchers.Main) {
                                        navController.navigate("Web")
                                    }

                                }

                            }
                        } catch (e: Exception) {
                        }
                    }
                })
                NavHost(navController = navController, startDestination = "Load") {
                    composable("Load") {
                        Load {
                            if (!weAreGood.value)
                                navController.navigate("Game")
                        }
                    }
                    composable("Game") {
                        Game(navController)
                    }
                    composable("Web") {
                        val webik = remember {
                            WebView(this@MainActivity)
                        }
                        BackHandler {
                            if (webik.canGoBack())
                                webik.goBack()
                        }
                        AndroidView(
                            factory = {
                                webik.apply {
                                    settings.apply {
                                        javaScriptEnabled = true
                                        useWideViewPort = true
                                        domStorageEnabled = true
                                        displayZoomControls = true
                                    }
                                    webik.webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            CookieManager.getInstance().flush()
                                        }
                                    }
                                    webik.webChromeClient = object : WebChromeClient() {
                                    }
                                    CookieManager.getInstance().acceptCookie()
                                    CookieManager.getInstance().setAcceptCookie(true)
                                    CookieManager.getInstance()
                                        .setAcceptThirdPartyCookies(webik, true)
                                    webik.loadUrl("https://google.com")
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )


                    }
                }
            }
        }
    }
}

