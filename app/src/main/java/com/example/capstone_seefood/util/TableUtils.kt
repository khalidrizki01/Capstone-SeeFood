package com.example.capstone_seefood.util

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale

fun addTextViewToTableRow(context: Context, tableRow: TableRow, text: String) {
    val textView = TextView(context)
    textView.text = text
    val params = TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.WRAP_CONTENT
    )
    params.gravity = Gravity.CENTER // Menetapkan gravity ke tengah
    textView.layoutParams = params
    tableRow.addView(textView)
}

fun formatNumber(number: Int): String {
    val formatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
    return formatter.format(number.toLong())
}