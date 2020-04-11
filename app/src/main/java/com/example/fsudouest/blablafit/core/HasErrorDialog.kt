package com.example.fsudouest.blablafit.core

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

interface HasErrorDialog {

    var dialog: AlertDialog?

    fun showDialog(
            context: Context,
            @StringRes title: Int,
            @StringRes message: Int,
            @StringRes positiveText: Int? = null,
            @StringRes negativeText: Int? = null,
            positiveListener: DialogInterface.OnClickListener? = DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() },
            negativeListener: DialogInterface.OnClickListener? = DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() },
            dismissListener: DialogInterface.OnDismissListener? = null
    ) {
        val titleString = context.getString(title)

        dialog?.let { if (it.isShowing) return }
        val builder = AlertDialog.Builder(context)

        builder.apply {
            setTitle(titleString)
            setMessage(message)
            positiveText?.let { setPositiveButton(positiveText, positiveListener) }
            negativeText?.let { setNegativeButton(negativeText, negativeListener) }
            dismissListener?.let { setOnDismissListener(dismissListener) }
            setCancelable(false)
        }
        dialog = builder.create()
        dialog?.show()
    }
}