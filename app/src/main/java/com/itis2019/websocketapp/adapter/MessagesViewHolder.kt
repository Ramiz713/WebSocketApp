package com.itis2019.websocketapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis2019.websocketapp.R
import com.itis2019.websocketapp.entity.Message
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_message.*
import kotlinx.android.synthetic.main.item_user_message.*

sealed class MessagesViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer

data class UserMessageViewHolder(override val containerView: View) :
    MessagesViewHolder(containerView) {

    companion object {
        fun from(parent: ViewGroup): UserMessageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_user_message, parent, false)
            return UserMessageViewHolder(view)
        }
    }

    fun bind(message: Message) {
        chat_user_message_tv.text = message.message
    }
}

data class ChatMessageViewHolder(override val containerView: View) :
    MessagesViewHolder(containerView) {

    companion object {
        fun from(parent: ViewGroup): ChatMessageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_chat_message, parent, false)
            return ChatMessageViewHolder(view)
        }
    }

    fun bind(message: Message) {
        chat_message_sender_name_tv.text = message.user
        chat_message_tv.text = message.message
    }
}
