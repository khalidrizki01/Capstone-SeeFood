package com.example.capstone_seefood

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_seefood.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            print("helo")
            barChart?.animation!!.duration = animationDuration
            barChart?.animate(barSet)
        }

        binding.btnMingguan.setOnClickListener {
            startActivity(Intent(this@MainActivity, BarChartActivityMingguan::class.java))
        }
        binding.btnBulanan.setOnClickListener {
            startActivity(Intent(this@MainActivity, BarChartActivityBulanan::class.java))
        }

        val list:ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(100f,"100"))
        list.add(PieEntry(101f,"101"))
        list.add(PieEntry(102f,"102"))
        list.add(PieEntry(103f,"103"))
        list.add(PieEntry(104f,"104"))

        val pieDataSet= PieDataSet(list,"List")

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        pieDataSet.valueTextColor= Color.BLACK
        pieDataSet.valueTextSize=15f

        val pieData= PieData(pieDataSet)

        binding.pieChart!!.data= pieData

        binding.pieChart!!.description.text= "Pie Chart"

        binding.pieChart!!.centerText="List"

        binding.pieChart!!.animateY(2000)
        //add
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        var totaljual = arrayOf(4,7,2,3,5,4)
        var dictionary = arrayOf("0f","0f","0f","0f","0f")
        private val barSet = listOf(
            "Ayam Guling Kukus \n (Terjual : ${totaljual[0]})" to 2F,
            "Babi Panggang " to 7F,
            "Bakpia Bakar Mozarella" to 2F,
            "Sapi Geprek Sambal Terasi" to 2.3F,
            "Pisang Tumis Blackpepper" to 5F,
            "Kerang Saus Mentai" to 4F,
            "momogi" to 2F
        )


        private const val animationDuration = 1000L
    }
}