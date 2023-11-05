package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class ConfirmPaymentActivity : AppCompatActivity() {

    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_payment)

        val tableLayout = findViewById<TableLayout>(R.id.tbOrder)
        var count: Int = 1
        for (rowData in data) {
            val tableRow = TableRow(this)

            for (item in rowData) {
                val textView = TextView(this)
                textView.text = item
                textView.gravity = Gravity.START
                textView.setPadding(5, 5, 5, 5)
                tableRow.addView(textView)

            }
            tableLayout.addView(tableRow, count)
            count++
        }
    }
}