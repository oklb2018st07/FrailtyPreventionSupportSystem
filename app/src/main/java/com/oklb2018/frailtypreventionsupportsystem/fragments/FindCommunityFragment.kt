package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.sub_activity_main_find_community.*


class FindCommunityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_find_community, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        webView.settings.setGeolocationEnabled(true)
        webView.settings.javaScriptEnabled = true
        */
        //webView.loadUrl("https://www.google.co.jp")
        /*
        webView.webChromeClient = object: WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                if (callback != null) {
                    callback.invoke(origin, true, true)
                }
            }
        }
        */
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1i3M4ta-oOkB14_u-8lIliLIq6_UlR97q&usp=sharing")))
        Log.d("debug", "Goto My Maps")
    }
}