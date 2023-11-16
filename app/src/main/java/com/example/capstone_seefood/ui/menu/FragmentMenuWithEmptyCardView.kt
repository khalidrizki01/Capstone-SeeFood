package com.example.capstone_seefood.ui.menu

//import android.os.Bundle
//import android.provider.Settings.Global
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import com.example.capstone_seefood.R
//import com.example.capstone_seefood.db.Food
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import com.example.capstone_seefood.db.FoodDao
//import com.example.capstone_seefood.db.FoodDatabase
//import com.google.android.material.card.MaterialCardView
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
////// TODO: Rename parameter arguments, choose names that match
////// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////private const val ARG_PARAM1 = "param1"
////private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [FragmentMenuWithEmptyCardView.newInstance] factory method to
// * create an instance of this fragment.
// */
//class FragmentMenuWithEmptyCardView : Fragment() {
//    // TODO: Rename and change types of parameters
////    private var param1: String? = null
////    private var param2: String? = null
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
////    }
//    private lateinit var foodDao : FoodDao
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        foodDao = FoodDatabase.getInstance(requireContext()).foodDao
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_with_empty_card_view, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        GlobalScope.launch {
//            val availableFoods = foodDao.getAvailableFood()
//            val unavailableFoods = foodDao.getUnavailableFood()
//
//            withContext(Dispatchers.Main) {
//                displayFoodCards(availableFoods, R.id.linAvailableFood)
//                displayFoodCards(unavailableFoods, R.id.linUnavailableFood)
//            }
//        }
//    }
//
//    private fun displayFoodCards(foodList: List<Food>, linearLayoutId: Int) {
//        val linearLayout = view?.findViewById<LinearLayout>(linearLayoutId)
//
//        // Membersihkan linear layout sebelum menambahkan card view baru
//        linearLayout?.removeAllViews()
//
//        // Iterasi melalui daftar makanan dan menambahkan card view untuk setiap makanan
//        for (food in foodList) {
//            val cardView = createFoodCardView(food)
//            linearLayout?.addView(cardView)
//        }
//    }
//
//    private fun createFoodCardView(food: Food): MaterialCardView {
//        val cardView = MaterialCardView(requireContext())
//        val layoutParams = LinearLayout.LayoutParams(
//            0, // Atur sesuai kebutuhan
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.weight = 1f
//        cardView.layoutParams = layoutParams
//        cardView.cardElevation = 8f
//        cardView.radius = 16f
//
//        // Inisialisasi tampilan dalam card view (ImageView, TextView, dll.) sesuai kebutuhan
//        // ...
//
//        return cardView
//    }
//
////    companion object {
////        /**
////         * Use this factory method to create a new instance of
////         * this fragment using the provided parameters.
////         *
////         * @param param1 Parameter 1.
////         * @param param2 Parameter 2.
////         * @return A new instance of fragment FragmentMenuWithEmptyCardView.
////         */
////        // TODO: Rename and change types and number of parameters
////        @JvmStatic
////        fun newInstance(param1: String, param2: String) =
////            FragmentMenuWithEmptyCardView().apply {
////                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
////                }
////            }
////    }
//}