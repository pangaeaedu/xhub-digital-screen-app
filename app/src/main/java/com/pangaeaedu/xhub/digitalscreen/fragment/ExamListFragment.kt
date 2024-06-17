package com.pangaeaedu.xhub.digitalscreen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pangaeaedu.xhub.digitalscreen.R
import com.pangaeaedu.xhub.digitalscreen.databinding.FragmentExamListBinding
import com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter.ExamListAdapter
import com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter.XhubDividerItemDecoration

class ExamListFragment : Fragment() {
    private lateinit var mAdapter: ExamListAdapter
    private lateinit var mBinding: FragmentExamListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (savedInstanceState ?: arguments)?.apply {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentExamListBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ExamListAdapter()
        mBinding.recyclerView.addItemDecoration(
            XhubDividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL,
                R.drawable.divider_16_dp
            )
        )
        mBinding.recyclerView.adapter = mAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExamListFragment().apply {
        }
    }
}
