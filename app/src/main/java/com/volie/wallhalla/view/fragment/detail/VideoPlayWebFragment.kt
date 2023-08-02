package com.volie.wallhalla.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volie.wallhalla.databinding.FragmentVideoPlayWebBinding

class VideoPlayWebFragment : Fragment() {
    private var _mBinding: FragmentVideoPlayWebBinding? = null
    private val mBinding get() = _mBinding!!
    private val mArgs: VideoPlayWebFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentVideoPlayWebBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startWebView()

        mBinding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
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
        with(mBinding.webViewCollectionVideo) {
            webViewClient = CustomWebViewClient()
            loadUrl(mArgs.videoUrl.url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}