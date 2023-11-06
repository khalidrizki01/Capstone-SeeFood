package com.example.capstone_seefood
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.room.Room
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.databinding.ActivityHistoryPenjualanBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryPenjualanActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryPenjualanBinding

    val solditems = listOf(
        listOf("Pay#0001", "19 Agustus 07:02", "Rp 25,000", "Cash"),
        listOf("Pay#0002", "19 Agustus 08:10", "Rp 52,000", "OVO"),
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