package com.example.capstone_seefood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_seefood.databinding.ActivityHistoryPenjualanBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.FoodSum
import java.time.DayOfWeek
import java.time.LocalDate

class HistoryPenjualanActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryPenjualanBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentIDList: ArrayList<PaymentID>
    private lateinit var paymentIDAdapter: PaymentIDAdapter
    private lateinit var foodDao: FoodDao
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
        foodDao = FoodDatabase.getInstance(this).foodDao

        binding = ActivityHistoryPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.rvPaymentID)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val arraypayment: ArrayList<Receipt> = ArrayList(getSalesToday())
        paymentIDList = ArrayList()

        paymentIDAdapter = PaymentIDAdapter(arraypayment)
        recyclerView.adapter = paymentIDAdapter

        binding.btnHistoryMinggu.setOnClickListener {
            val arraypayment: ArrayList<Receipt> = ArrayList(getSalesThisWeek())
            paymentIDList = ArrayList()

            paymentIDAdapter = PaymentIDAdapter(arraypayment)
            recyclerView.adapter = paymentIDAdapter
            paymentIDAdapter.onItemClick={
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("paymentid",it)
                startActivity(intent)
            }

        }
        binding.btnHistoryBulan.setOnClickListener {
            val arraypayment: ArrayList<Receipt> = ArrayList(getSalesThisMonth())
            paymentIDList = ArrayList()

            paymentIDAdapter = PaymentIDAdapter(arraypayment)
            recyclerView.adapter = paymentIDAdapter
            paymentIDAdapter.onItemClick={
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("paymentid",it)
                startActivity(intent)
            }

        }
        binding.button.setOnClickListener {
            val arraypayment: ArrayList<Receipt> = ArrayList(getSalesToday())
            paymentIDList = ArrayList()

            paymentIDAdapter = PaymentIDAdapter(arraypayment)
            recyclerView.adapter = paymentIDAdapter
            paymentIDAdapter.onItemClick={
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("paymentid",it)
                startActivity(intent)
            }

        }
        paymentIDAdapter.onItemClick={
            val intent = Intent(this, ReceiptActivity::class.java)
            intent.putExtra("paymentid",it)
            startActivity(intent)
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

    private fun getSalesToday(): List<Receipt> {

        foodDao = FoodDatabase.getInstance(this).foodDao
        val today = LocalDate.now().atStartOfDay()
        return foodDao.getReceiptFrom(today)
    }
    private fun getSalesThisMonth(): List<Receipt> {
        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        return foodDao.getReceiptFrom(startOfMonth)
    }

    private fun getSalesThisWeek(): List<Receipt> {
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()
        return foodDao.getReceiptFrom(startOfWeek)
    }

}