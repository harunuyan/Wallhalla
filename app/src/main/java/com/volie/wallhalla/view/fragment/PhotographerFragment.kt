package com.volie.wallhalla.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.volie.wallhalla.databinding.FragmentPhotographerBinding

class PhotographerFragment : Fragment() {
    private var _mBinding: FragmentPhotographerBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var args: PhotographerFragmentArgs

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

        arguments?.let {
            args = PhotographerFragmentArgs.fromBundle(it)
        }

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
        with(mBinding) {
            webViewPhotographer.webViewClient = CustomWebViewClient()
            webViewPhotographer.loadUrl(args.photographerUrl.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}