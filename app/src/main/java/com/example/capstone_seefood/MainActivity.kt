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
    private lateinit var foodDao: FoodDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = FoodDatabase.getInstance(this).foodDao
        GlobalScope.launch {
            storeReceipt()
            var listFoodSum = getSalesThisMonth()
            var listSale = listFoodSum.map { Pair(it.name, it.sum.toFloat()) }

//            Implementasi Bar Chart
            barSet = listSale
            val animationDuration = 1000L
            Log.d("CHART", "sampe sini")
            binding.apply {
                barChart?.animation!!.duration = animationDuration
                barChart?.animate(barSet)
            }

//            Implementasi Pie Chart
            val pieDataSet = PieDataSet(listFoodSum.map { PieEntry(it.sum.toFloat(), it.name) }, "List")
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
            pieDataSet.valueTextColor= Color.BLACK
            pieDataSet.valueTextSize=15f
            val pieData= PieData(pieDataSet)
            binding.pieChart!!.data= pieData

            binding.pieChart!!.description.text= "Pie Chart"

            binding.pieChart!!.centerText="List"
//


            GlobalScope.launch(Dispatchers.Main) {
                binding.pieChart!!.animateY(2000)
            }
            val (favoritMenu, mostIncome) = getTopSoldFoodName(listFoodSum)
            binding.tvFavoriteMenu!!.text = favoritMenu
            binding.tvTotalRevenue!!.text = "Rp${String.format("%,d", mostIncome)}"
        }


        getPermission()



        binding.btnMingguan.setOnClickListener {
            startActivity(Intent(this@MainActivity, BarChartActivityMingguan::class.java))
        }
        binding.btnBulanan.setOnClickListener {
            startActivity(Intent(this@MainActivity, BarChartActivityBulanan::class.java))
        }
        binding.btnPenjualan.setOnClickListener {
            startActivity(Intent(this@MainActivity, manage_price::class.java))
        }
        binding.btnRiwayatTransaksi.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryPenjualanActivity::class.java))
        }
        binding.btnModeScan.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanActivity::class.java))
        }


//        val list:ArrayList<PieEntry> = ArrayList()
//
//        list.add(PieEntry(100f,"100"))
//        list.add(PieEntry(101f,"101"))
//        list.add(PieEntry(102f,"102"))
//        list.add(PieEntry(103f,"103"))
//        list.add(PieEntry(104f,"104"))
//
//        val pieDataSet= PieDataSet(list,"List")
//
//        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
//        pieDataSet.valueTextColor= Color.BLACK
//        pieDataSet.valueTextSize=15f
//
//        val pieData= PieData(pieDataSet)
//
//        binding.pieChart!!.data= pieData
//
//        binding.pieChart!!.description.text= "Pie Chart"
//
//        binding.pieChart!!.centerText="List"
//
//        binding.pieChart!!.animateY(2000)

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

    companion object {
//        var totaljual = arrayOf(4,7,2,3,5,4)
//        var dictionary = arrayOf("0f","0f","0f","0f","0f")
//        private val barSet = listOf(
//            "Ayam Guling Kukus \n (Terjual : ${totaljual[0]})" to 2F,
//            "Babi Panggang " to 7F,
//            "Bakpia Bakar Mozarella" to 2F,
//            "Sapi Geprek Sambal Terasi" to 2.3F,
//            "Pisang Tumis Blackpepper" to 5F,
//            "Kerang Saus Mentai" to 4F,
//            "momogi" to 2F
//        )
    }

    private fun getSalesToday() : List<FoodSum>{
        val today = LocalDate.now().atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(today)
    }
    private fun getSalesThisMonth() : List<FoodSum>{
        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfMonth)
    }

    private fun getSalesThisWeek() : List<FoodSum> {
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfWeek)
    }

    private fun getSalesThisYear() : List<FoodSum>{
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
    private fun storeReceipt() {
//        foods.forEach { foodDao.insertFood(it) }

        val listOfReceipt = listOf(
            listOf("Nasi", "Ayam Goreng", "Tahu"),
            listOf("Nasi", "Tempe"),
            listOf("Nasi", "Ayam Goreng", "Tempe")
        )
        val receiptFoodQuantities = listOf(
            listOf(1,1,1),
            listOf(1, 3),
            listOf(1, 1, 2)
        )
        for((i, receipt) in listOfReceipt.withIndex()){
            for((index, identifiedFood) in receipt.withIndex()) {
                var foodQty = receiptFoodQuantities[i]
                var recId : UUID = UUID.randomUUID()
                var totalPrice : Int = 0
                var oneFood = foodDao.getFoodBasedOnName(identifiedFood)
                if(oneFood.isSell) {
                    var receiptFood = ReceiptFoodCrossRef(recId, oneFood.foodId, foodQty[index])
                    totalPrice += receiptFood.calculateTotalItemPrice(oneFood.price!!)
                    foodDao.insertReceiptFoodCrossRef(receiptFood)
                } else {
                    continue
                }
                var newReceipt = Receipt(recId, totalPrice)
                foodDao.insertReceipt(newReceipt)
            }
        }



    }
}