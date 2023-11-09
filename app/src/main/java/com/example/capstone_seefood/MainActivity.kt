package com.example.capstone_seefood

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_seefood.ChartData.BarChartActivityBulanan
import com.example.capstone_seefood.ChartData.BarChartActivityMingguan
import com.example.capstone_seefood.databinding.ActivityMainBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.FoodSum
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodDao : FoodDao
    private lateinit var barSet : List<Pair<String, Float>>
    private var currentChartType: ChartType = ChartType.WEEKLY // Default chart type
    enum class ChartType {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = FoodDatabase.getInstance(this).foodDao
        GlobalScope.launch {
            foodDao.deleteAllReceipts()
            foodDao.deleteAllReceiptFoodCR()

            storeReceipt()

            updateCharts()

            binding.btnHarian.setOnClickListener {
                currentChartType = ChartType.DAILY
                GlobalScope.launch{
                    updateCharts()
                }
                Log.d("CHART", "VISUALISASI DAILY")
            }

            binding.btnMingguan.setOnClickListener {
                currentChartType = ChartType.WEEKLY
                GlobalScope.launch{
                    updateCharts()
                }
//            startActivity(Intent(this@MainActivity, BarChartActivityMingguan::class.java))
                Log.d("CHART", "VISUALISASI WEEKLY")
            }
            binding.btnBulanan.setOnClickListener {
                currentChartType = ChartType.MONTHLY
                GlobalScope.launch{
                    updateCharts()
                }
//            startActivity(Intent(this@MainActivity, BarChartActivityBulanan::class.java))
                Log.d("CHART", "VISUALISASI MONTHLY")
            }
        }

//        getPermission()

        binding.btnPenjualan.setOnClickListener {
            startActivity(Intent(this@MainActivity, manage_price::class.java))
        }
        binding.btnRiwayatTransaksi.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryPenjualanActivity::class.java))
        }
        binding.btnModeScan.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanActivity::class.java))
        }
    }
  
    fun getPermission() {
        var hwaccess = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            hwaccess.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            hwaccess.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            hwaccess.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (hwaccess.size > 0) {
            requestPermissions(hwaccess.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                getPermission()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private suspend fun updateCharts() {
//        GlobalScope.launch {
        if (currentChartType == ChartType.DAILY) { Log.d("TYPE", "VISUALISASI DAILY")}
        val listFoodSum = when (currentChartType) {
            ChartType.DAILY -> getSalesToday()
            ChartType.WEEKLY -> getSalesThisWeek()
            ChartType.MONTHLY -> getSalesThisMonth()
        }
            // Update bar chart
        barSet = listFoodSum.map { Pair(it.name, it.sum.toFloat()) }
        val animationDuration = 1000L
        binding.apply {
            barChart?.animation!!.duration = animationDuration
            barChart?.animate(barSet)
        }
            // Update pie chart
        val pieDataSet = PieDataSet(listFoodSum.map { PieEntry(it.sum.toFloat(), it.name) }, "List")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f
        val pieData = PieData(pieDataSet)
        binding.pieChart!!.data = pieData
        binding.pieChart!!.description.text = "Pie Chart"
        binding.pieChart!!.centerText = "List"

        GlobalScope.launch(Dispatchers.Main) {
            binding.pieChart?.animateY(2000)
        }

        val (favoritMenu, mostIncome) = getTopSoldFoodName(listFoodSum)
        binding.tvFavoriteMenu?.text = favoritMenu
        binding.tvTotalRevenue?.text = "Rp${String.format("%,d", mostIncome)}"
    }

    private suspend fun getSalesToday(): List<FoodSum> {
        val today = LocalDate.now().atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(today)
    }

    private suspend fun getSalesThisMonth(): List<FoodSum> {
        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfMonth)
    }

    private suspend fun getSalesThisWeek(): List<FoodSum> {
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfWeek)
    }

    private suspend fun getSalesThisYear(): List<FoodSum> {
        val startOfYear = LocalDate.now().withDayOfYear(1).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfYear)
    }

    fun getTopSoldFoodName(listSale: List<FoodSum>): Pair<String, Int> {
        // Temukan food dengan sum terbanyak
        val topSoldFood = listSale.maxBy { it.sum }
            // Kembalikan name dari food tersebut
        return Pair(topSoldFood?.name ?: "", topSoldFood?.sum ?: 0)
    }

//    Inisialisasi data sales
    private suspend fun storeReceipt() {
//        GlobalScope.launch {
        val listOfReceipt = listOf(
            listOf("Nasi", "Ayam Goreng", "Tahu"),
            listOf("Nasi", "Tempe"),
            listOf("Nasi", "Ayam Goreng", "Tempe"),
            listOf("Tempe", "Tahu"),
            listOf("Nasi", "Tahu")
        )
        val receiptFoodQuantities = listOf(
            listOf(1, 1, 1),
            listOf(1, 3),
            listOf(1, 1, 2),
            listOf(50, 1),
            listOf(1, 40)
        )

        for ((i, receipt) in listOfReceipt.withIndex()) {
            for ((index, identifiedFood) in receipt.withIndex()) {
                var foodQty = receiptFoodQuantities[i]
                var recId: UUID = UUID.randomUUID()
                var totalPrice: Int = 0
                var oneFood = foodDao.getFoodBasedOnName(identifiedFood)
                if (oneFood.isSell) {
                    var receiptFood = ReceiptFoodCrossRef(recId, oneFood.foodId, oneFood.name, oneFood.price, foodQty[index])
                    totalPrice += receiptFood.calculateTotalItemPrice(oneFood.price!!)
                    foodDao.insertReceiptFoodCrossRef(receiptFood)
                } else {
                    continue
                }

                val dates = listOf(
                    LocalDate.parse("2023-11-06").atStartOfDay(),
                    LocalDate.parse("2023-11-01").atStartOfDay()
                )
                var newReceipt: Receipt
                if (i >= 3) {
                    newReceipt = Receipt(recId, totalPrice, dates[i - 3])
                } else {
                    newReceipt = Receipt(recId, totalPrice)
                }
                foodDao.insertReceipt(newReceipt)
            }
        }
//        }
    }
}

