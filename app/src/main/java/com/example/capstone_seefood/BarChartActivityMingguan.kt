package com.example.capstone_seefood
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.databinding.ActivityBarChartMingguanBinding
import com.example.capstone_seefood.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartActivityMingguan : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            print("helo")
            barChart.animation.duration = animationDuration
            barChart.animate(barSet)


        }
        binding.btnHarian?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityMingguan,MainActivity::class.java))
        }
        binding.btnBulanan?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityMingguan,BarChartActivityBulanan::class.java))
    }
        val list:ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(250f,"100"))
        list.add(PieEntry(511f,"101"))
        list.add(PieEntry(202f,"102"))
        list.add(PieEntry(153f,"103"))
        list.add(PieEntry(214f,"104"))

        val pieDataSet= PieDataSet(list,"List")

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        pieDataSet.valueTextColor= Color.BLACK
        pieDataSet.valueTextSize=15f

        val pieData= PieData(pieDataSet)

        binding.pieChart!!.data= pieData

        binding.pieChart!!.description.text= "Pie Chart"

        binding.pieChart!!.centerText="List"

        binding.pieChart!!.animateY(2000)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        var totaljual = arrayOf(4,7,2,3,5,4)
        private val barSet = listOf(
            "Ayam Guling Kukus \n${totaljual[0]}" to 40F,
            "Babi Panggang" to 7F,
            "Bakpia Bakar Mozarella" to 2F,
            "Sapi Geprek Sambal Terasi" to 2.3F,
            "Pisang Tumis Blackpepper" to 5F,
            "Kerang Saus Mentai" to 4F
        )


        private const val animationDuration = 1000L
    }
}