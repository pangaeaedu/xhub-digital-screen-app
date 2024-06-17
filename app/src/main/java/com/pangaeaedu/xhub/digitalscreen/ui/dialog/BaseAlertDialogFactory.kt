package com.pangaeaedu.xhub.digitalscreen.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.pangaeaedu.xhub.digitalscreen.R

object BaseAlertDialogFactory {
    fun showOkDialog(
        activity: Activity?,
        titleResId: Int,
        messageResId: Int,
        onClickListener: ((DialogInterface, Int) -> Unit)?,
        onDismissListener: ((DialogInterface) -> Unit)?
    ): AlertDialog? {
        if (activity.canShowDialog()) {
            val builder: AlertDialog.Builder = getBuilder(activity)
            if (titleResId > 0) {
                builder.setTitle(titleResId)
            }
            builder.setMessage(messageResId)
            builder.setPositiveButton(android.R.string.ok, onClickListener)
            builder.setOnDismissListener(onDismissListener)
            val dlg = builder.create()
            dlg.show()
            return dlg
        }
        return null
    }

    fun showConfirmActionDialog(
        activity: Activity?, titleResId: Int, messageResId: Int,
        positiveButtonResId: Int,
        listener: ((DialogInterface, Int) -> Unit)?
    ): AlertDialog? {
        if (activity.canShowDialog()) {
            val dlg = getBuilder(activity)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(positiveButtonResId, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create()
            dlg.show()
            return dlg
        }
        return null
    }

    internal fun getBuilder(context: Context?): AlertDialog.Builder {
        if (context == null) {
            throw IllegalStateException("context is null")
        }
        return AlertDialog.Builder(context, R.style.PangaeaAlertDialog)
    }

    private fun Activity?.canShowDialog(): Boolean {
        return this != null && !this.isFinishing
    }
}
