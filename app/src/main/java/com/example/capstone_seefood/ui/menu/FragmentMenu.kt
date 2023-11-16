package com.example.capstone_seefood.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.capstone_seefood.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

private fun LayoutInflater.inflate(fragmentMenu: Int) {

}

private fun ImageView.setImageResource(name: CharSequence?) {

}

class FragmentMenu(val name: CharSequence) : Fragment() {

    private val price: String = ""
    private val imageResource: Int = 0
    private lateinit var scrollView: NestedScrollView
    private lateinit var chipGroup: ChipGroup


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        
        scrollView = view.findViewById(R.id.scrollview)
        chipGroup = view.findViewById(R.id.chipGroup)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollview)
        val cardViews = mutableListOf<MaterialCardView>()

        val menuList = listOf(
            FragmentMenu("Mie Goreng", R.drawable.menumakan, "Rp 6000"),
            FragmentMenu("Mie Goreng", R.drawable.menumakan, "Rp 6000"),
            FragmentMenu("Mie Goreng", R.drawable.menumakan, "Rp 6000")
        )
        
        for (menu in menuList){
            val chip = Chip(requireContext())
            chip.text = menu.name
            chipGroup.addView(chip)
        }
        
        for (menu in menuList){
            val cardView = LayoutInflater.from(requireContext()).inflate(
                R.layout.fragment_menu,
                scrollView,
                false) as MaterialCardView
            cardView.findViewById<ImageView>(R.id.imageViewItem).setImageResource(menu.imageResource)
            cardView.findViewById<TextView>(R.id.textViewItemName).text= menu.name
            cardView.findViewById<TextView>(R.id.textViewItemPrice).text= menu.price
            
        }
        return TODO("Provide the return value")
    }

    private fun FragmentMenu(s: String, menumakan: Int, s1: String): FragmentMenu {

        return TODO("Provide the return value")
    }
}
    // Anda dapat menambahkan lebih banyak logika ke dalam metode ini sesuai kebutuhan Anda
