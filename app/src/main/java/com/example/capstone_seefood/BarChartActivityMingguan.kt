package com.example.capstone_seefood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.capstone_seefood.databinding.ActivityBarChartBinding

class BarChartActivityMingguan : AppCompatActivity() {

    private var _binding: ActivityBarChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBarChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            print("helo")
            barChart.animation.duration = animationDuration
            barChart.animate(barSet)


        }
        binding.btnHarian?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityMingguan,BarChartActivity::class.java))
        }
        binding.btnBulanan?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityMingguan,BarChartActivityBulanan::class.java))
    }
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