package com.example.capstone_seefood.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentMenuViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Menu Fragment"
    }
    val text: LiveData<String> = _text
    val name: String = "Nasi Goreng"
    val price: String = "Rp12.000"
//    val imageResId: //Resource ID for the menu item image
}
