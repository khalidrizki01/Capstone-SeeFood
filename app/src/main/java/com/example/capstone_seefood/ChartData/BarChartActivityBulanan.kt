package com.example.capstone_seefood.ChartData
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.MainActivity
import com.example.capstone_seefood.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartActivityBulanan : AppCompatActivity() {

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
            startActivity(Intent(this@BarChartActivityBulanan, MainActivity::class.java))
        }
        binding.btnMingguan?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityBulanan, BarChartActivityMingguan::class.java))
    }
        val list:ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(230f,"100"))
        list.add(PieEntry(111f,"101"))
        list.add(PieEntry(412f,"102"))
        list.add(PieEntry(253f,"103"))
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
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        var totaljual = arrayOf(4,7,2,3,5,4)
        private val barSet = listOf(
            "Ayam Guling Kukus \n${totaljual[0]}" to 10F,
            "Babi Panggang" to 27F,
            "Bakpia Bakar Mozarella" to 24F,
            "Sapi Geprek Sambal Terasi" to 223F,
            "Pisang Tumis Blackpepper" to 1F,
            "Kerang Saus Mentai" to 11F
        )


        private const val animationDuration = 1000L
    }
}