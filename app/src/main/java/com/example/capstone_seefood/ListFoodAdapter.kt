package com.example.capstone_seefood

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.capstone_seefood.db.Food
//
//class ListFoodAdapter(private val listFood: ArrayList<Food>): RecyclerView.Adapter<ListFoodAdapter.ListViewHolder>() {
//    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgFood : ImageView = itemView.findViewById(R.id.imgFood)
//        val tvName : TextView = itemView.findViewById(R.id.tvFoodName)
//        val tvPrice : TextView = itemView.findViewById(R.id.tvHargaItem)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
//        return ListViewHolder(view)
//
//    }
//
//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val (foodId, name, photo, price, isSell) = listFood[position]
//        holder.imgFood.setImageResource(photo)
//        holder.tvName.text = name
//        holder.tvPrice.text = price!!.toString()
//    }
//}