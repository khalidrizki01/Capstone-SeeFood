package com.example.capstone_seefood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_seefood.databinding.ActivityHistoryPenjualanBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.FoodSum
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class HistoryPenjualanActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryPenjualanBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentIDAdapter: PaymentIDAdapter
    private lateinit var paymentIDList: ArrayList<Receipt>
    private lateinit var foodDao : FoodDao
    private var currentHistoryType: HistoryType = HistoryType.DAILY // Default chart type
    enum class HistoryType {
        DAILY,
        WEEKLY,
        MONTHLY
    }
//    val solditems = listOf(
//        listOf("Pay#0001", "19 Agustus 07:02", "Rp 25,000", "Cash"),
//        listOf("Pay#0002", "19 Agustus 08:10", "Rp 52,000", "OVO"),
//        listOf("Pay#0003", "19 Agustus 09:45", "Rp 30,000", "QRIS BCA"),
//        listOf("Pay#0004", "19 Agustus 10:20", "Rp 40,000", "GoPay"),
//        listOf("Pay#0005", "19 Agustus 12:15", "Rp 18,000", "Cash"),
//        listOf("Pay#0006", "19 Agustus 14:30", "Rp 55,000", "OVO"),
//        listOf("Pay#0007", "19 Agustus 16:05", "Rp 27,000", "QRIS BNI")
//
//    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CEK INI", "masuk")
        binding = ActivityHistoryPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recyclerView = findViewById(R.id.rvPaymentID)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        GlobalScope.launch{
            updateHistory()
        }
        recyclerView.adapter = paymentIDAdapter
        paymentIDAdapter.onItemClick = {
            val intent = Intent(this, ReceiptActivity::class.java)
            intent.putExtra("paymentid", it)
            startActivity(intent)
        }

        binding.btnHistoryMinggu.setOnClickListener {
            currentHistoryType =HistoryType.WEEKLY
            GlobalScope.launch{
                updateHistory()
            }
        }


//        var count: Int = 1
//        for (rowData in solditems) {
//            val tableRow = TableRow(this)
//            for (item in rowData) {
//                val textView = TextView(this)
//                textView.text = item
//                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
//                textView.gravity = Gravity.START
//                textView.setPadding(5, 5, 5, 5)
//                tableRow.addView(textView)
//            }
//            binding.tbOrder.addView(tableRow, count)
//            count++
//        }

        binding.btnbackhome.setOnClickListener {
            startActivity(Intent(this@HistoryPenjualanActivity,MainActivity::class.java))
        }


    }
    private suspend fun updateHistory() {
        val paymentIDList = when (currentHistoryType) {
            HistoryType.DAILY -> getSalesToday()
            HistoryType.WEEKLY -> getSalesThisWeek()
            HistoryType.MONTHLY -> getSalesThisMonth()
        }
        val arrayPaymentID : ArrayList<Receipt> = ArrayList(paymentIDList)
        Log.d("CEK INI", arrayPaymentID.toString())
        paymentIDAdapter = PaymentIDAdapter(arrayPaymentID)

    }
    private suspend fun getSalesToday(): List<Receipt> {
        val today = LocalDate.now().atStartOfDay()
        return foodDao.getReceiptFrom(today)
    }
    private suspend fun getSalesThisMonth(): List<Receipt> {
        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        return foodDao.getReceiptFrom(startOfMonth)
    }

    private suspend fun getSalesThisWeek(): List<Receipt> {
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()
        return foodDao.getReceiptFrom(startOfWeek)
    }
}