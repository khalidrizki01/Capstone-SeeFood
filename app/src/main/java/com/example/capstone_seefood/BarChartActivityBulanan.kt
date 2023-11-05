package com.example.capstone_seefood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.databinding.ActivityBarChartBinding

class BarChartActivityBulanan : AppCompatActivity() {

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
            startActivity(Intent(this@BarChartActivityBulanan,MainActivity::class.java))
        }
        binding.btnMingguan?.setOnClickListener{
            startActivity(Intent(this@BarChartActivityBulanan,BarChartActivityMingguan::class.java))
    }
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