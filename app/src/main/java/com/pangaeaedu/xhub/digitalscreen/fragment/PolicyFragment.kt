package com.pangaeaedu.xhub.digitalscreen.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.pangaeaedu.xhub.digitalscreen.MainActivityCallback
import com.pangaeaedu.xhub.digitalscreen.R
import com.pangaeaedu.xhub.digitalscreen.constant.Key
import com.pangaeaedu.xhub.digitalscreen.core.AppCore
import com.pangaeaedu.xhub.digitalscreen.core.applyArguments
import com.pangaeaedu.xhub.digitalscreen.core.getStringById
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.databinding.PolicyFragmentBinding

class PolicyFragment : Fragment() {
    private lateinit var mBinding: PolicyFragmentBinding
    private var mExamInfo: ExamSessionInfo? = null

    private val mUrl = "https://www.pangaeaedu.com/terms-of-service-privacy-policy.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (savedInstanceState ?: arguments)?.apply {
            mExamInfo = getParcelable(Key.KEY_EXAM_INFO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = PolicyFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mExamInfo?.let { itExamInfo ->
            mBinding.webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    url?.let {
                        view.loadUrl(it)
                    }
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    beginToCountDown()
                }
            }
            mBinding.webView.loadUrl(mUrl)
            mBinding.boxAgree.setOnCheckedChangeListener { _, isChecked ->
                checkCanNext()
            }
            mBinding.tvAgree.setOnClickListener {
                mBinding.boxAgree.isChecked = !mBinding.boxAgree.isChecked
                checkCanNext()
            }
            mBinding.btnNext.setOnClickListener {
                (requireActivity() as MainActivityCallback).onGotoExamInfoFragment(itExamInfo)
            }
        }
    }

    private var COUNT_DOWN_DURATION = 5
    private val mHandler = Handler(Looper.getMainLooper())
    private fun beginToCountDown() {
        mBinding.btnNext.text = AppCore.getStringById(R.string.next_with_count, COUNT_DOWN_DURATION)
        countDown()
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(countDownRunner)
    }

    private val countDownRunner = Runnable {
        COUNT_DOWN_DURATION--
        if (COUNT_DOWN_DURATION <= 0) {
            mBinding.btnNext.text = AppCore.getStringById(R.string.next)
            checkCanNext()
        } else {
            mBinding.btnNext.text =
                AppCore.getStringById(R.string.next_with_count, COUNT_DOWN_DURATION)
            countDown()
        }
    }

    private fun countDown() {
        mHandler.postDelayed(countDownRunner, 1000)
    }

    private fun checkCanNext() {
        mBinding.btnNext.isEnabled = COUNT_DOWN_DURATION <= 0 && mBinding.boxAgree.isChecked
    }


    companion object {
        @JvmStatic
        fun newInstance(examInfo: ExamSessionInfo) = PolicyFragment().applyArguments {
            putParcelable(Key.KEY_EXAM_INFO, examInfo)
        }
    }
}
