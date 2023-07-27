package com.example.chatgptinkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ChatListAdapter(val chatList : List<ChatModel>) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout , parent ,false)

        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
       val listItem = chatList.get(position)
        if(listItem.isSent){
            holder.recCard.visibility = View.GONE
            holder.sendCard.visibility = View.VISIBLE
            holder.sentText.text= listItem.message
        }else{
            holder.recCard.visibility = View.VISIBLE
            holder.sendCard.visibility = View.GONE
            holder.recText.text= listItem.message
        }
    }


    class ChatViewHolder(itemView : View): ViewHolder(itemView)  {

        val sendCard = itemView.findViewById<CardView>(R.id.send_list_card)
        val recCard = itemView.findViewById<CardView>(R.id.recive_list_card)
        val sentText=itemView.findViewById<TextView>(R.id.tvSendMessage)
        val recText=itemView.findViewById<TextView>(R.id.tvReciveMessage)


    }
}