package com.example.capstone_seefood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_seefood.db.Receipt

class PaymentIDAdapter(private val paymentIDList:ArrayList<Receipt>):
    RecyclerView.Adapter<PaymentIDAdapter.PaymentIDViewHolder>() {
    var onItemClick: ((Receipt)-> Unit)? = null
    class PaymentIDViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewpayid: TextView = itemView.findViewById(R.id.tvpaymentID)
        val textdate: TextView = itemView.findViewById(R.id.tvDate)
        val texttprice: TextView = itemView.findViewById(R.id.tvTPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentIDViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payid, parent,false)
        return PaymentIDViewHolder(view)

    }

    override fun getItemCount(): Int {
        return paymentIDList.size
    }

    override fun onBindViewHolder(holder: PaymentIDViewHolder, position: Int) {
        val paymentid = paymentIDList[position]
        holder.textViewpayid.text = paymentid.receiptId.toString()//paymentID //receiptId.toString()
        holder.textdate.text = paymentid.createdAt.toString()//date //createdAt.toString()
        holder.texttprice.text = "Rp${paymentid.totalPrice.toString()}" //total //totalPrice.toString()
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(paymentid)
        }
    }
}