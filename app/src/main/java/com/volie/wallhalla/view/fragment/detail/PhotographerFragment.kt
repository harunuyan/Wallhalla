package com.volie.wallhalla.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.volie.wallhalla.databinding.FragmentPhotographerBinding

class PhotographerFragment : Fragment() {
    private var _mBinding: FragmentPhotographerBinding? = null
    private val mBinding get() = _mBinding!!
    private val mArgs: PhotographerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentPhotographerBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startWebView()
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

    private fun startWebView() {
        with(mBinding.webViewPhotographer) {
            webViewClient = CustomWebViewClient()
            loadUrl(mArgs.photographerUrl.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}