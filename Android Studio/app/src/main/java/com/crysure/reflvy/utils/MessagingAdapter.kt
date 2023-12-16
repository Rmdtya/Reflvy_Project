package com.crysure.reflvy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crysure.reflvy.databinding.TemplateChatBinding // Ubah impor sesuai dengan View Binding
import com.crysure.reflvy.data.Message
import com.crysure.reflvy.utils.Constrant.RECEIVE_ID
import com.crysure.reflvy.utils.Constrant.SEND_ID

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Message>()

    inner class MessageViewHolder(val binding: TemplateChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Tidak perlu lagi inisialisasi secara manual karena sudah diambil dari binding.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = TemplateChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.binding.answerUser.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.binding.templateBot.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.binding.answerBot.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.binding.templateUser.visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
        notifyDataSetChanged()
    }

}

