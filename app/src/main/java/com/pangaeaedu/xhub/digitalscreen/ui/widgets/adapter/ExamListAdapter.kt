package com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.viewholder.ExamListItemViewHolder

class ExamListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListExamSessionInfo: List<ExamSessionInfo> = emptyList()

    init {
        generateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExamListItemViewHolder(parent.createBinding())
    }

    override fun getItemCount(): Int {
        return mListExamSessionInfo.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExamListItemViewHolder).bindData(mListExamSessionInfo[position])
    }

    private fun generateList() {
        mutableListOf<ExamSessionInfo>().let{
            it.add(ExamSessionInfo(1L, 0, "Math",1001, "John Smith", System.currentTimeMillis() - 888 * 1000L, 1800 * 1000L, "In Service") )
            it.add(ExamSessionInfo(2L, 1, "Chinese", 1002, "Ken White", System.currentTimeMillis() + 4488 * 1000L, 1800 * 1000L,"Check In"))
            it.add(ExamSessionInfo(3L, 2, "English", 1003, "Jim Green", System.currentTimeMillis() + 8088 * 1000L, 1800 * 1000L,"Waiting"))
            it.add(ExamSessionInfo(4L, 3, "Science", 1004, "Tom Happy", System.currentTimeMillis() + 11688 * 1000L, 1800 * 1000L,"Waiting"))
            it.add(ExamSessionInfo(5L, 4, "Biology", 1005, "Amuluo Gundam ", System.currentTimeMillis() + 15288 * 1000L, 3600 * 1000L,"Waiting"))
            mListExamSessionInfo = it
        }
    }
}
