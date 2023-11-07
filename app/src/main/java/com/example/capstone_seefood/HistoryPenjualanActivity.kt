package com.example.capstone_seefood
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.databinding.ActivityHistoryPenjualanBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryPenjualanActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryPenjualanBinding

    val solditems = listOf(
        listOf("Pay#0001", "19 Agustus 07:02", "Rp 25,000", "Cash"),
        listOf("Pay#0002", "19 Agustus 08:10", "Rp 52,000", "OVO"),
        listOf("Pay#0003", "19 Agustus 09:45", "Rp 30,000", "QRIS BCA"),
        listOf("Pay#0004", "19 Agustus 10:20", "Rp 40,000", "GoPay"),
        listOf("Pay#0005", "19 Agustus 12:15", "Rp 18,000", "Cash"),
        listOf("Pay#0006", "19 Agustus 14:30", "Rp 55,000", "OVO"),
        listOf("Pay#0007", "19 Agustus 16:05", "Rp 27,000", "QRIS BNI")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var count: Int = 1
        for (rowData in solditems) {
            val tableRow = TableRow(this)
            for (item in rowData) {
                val textView = TextView(this)
                textView.text = item
                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                textView.gravity = Gravity.START
                textView.setPadding(5, 5, 5, 5)
                tableRow.addView(textView)
            }
            binding.tbOrder.addView(tableRow, count)
            count++
        }


//        binding.btnConfirmPayment.setOnClickListener(this)
    }
}