package com.WillyFisky.mobile911

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_view.view.*

class RequestRecyclerViewAdapter(private  val addRequest: MutableList<Request>): RecyclerView.Adapter<RequestRecyclerViewAdapter.RecyclerViewHolder>() {
    inner class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentState = addRequest[position]
        holder.itemView.apply {
            textHelp.text = currentState.request
            textLocation.text = currentState.location
        }
    }

    override fun getItemCount(): Int {
        return addRequest.size
    }

    fun addRequestFunction(request: Request){
        addRequest.add(request)
        notifyItemInserted(addRequest.size - 1)
        notifyDataSetChanged()
    }
}