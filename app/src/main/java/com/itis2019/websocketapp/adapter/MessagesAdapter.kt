package com.itis2019.websocketapp.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.itis2019.websocketapp.entity.ChatMessage
import com.itis2019.websocketapp.entity.MessageDataItem
import com.itis2019.websocketapp.entity.UserMessage

const val ITEM_VIEW_TYPE_USER_MESSAGE = 0
const val ITEM_VIEW_TYPE_CHAT_MESSAGE = 1

class MessagesAdapter: ListAdapter<MessageDataItem, MessagesViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)){
            is UserMessage -> ITEM_VIEW_TYPE_USER_MESSAGE
            is ChatMessage -> ITEM_VIEW_TYPE_CHAT_MESSAGE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder =
        when(viewType){
            ITEM_VIEW_TYPE_USER_MESSAGE -> UserMessageViewHolder.from(parent)
            ITEM_VIEW_TYPE_CHAT_MESSAGE -> ChatMessageViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) = when(holder){
        is UserMessageViewHolder -> holder.bind((getItem(position) as UserMessage).message)
        is ChatMessageViewHolder -> holder.bind((getItem(position) as ChatMessage).message)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageDataItem>() {

            override fun areItemsTheSame(
                oldItem: MessageDataItem,
                newItem: MessageDataItem
            ): Boolean = oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: MessageDataItem,
                newItem: MessageDataItem
            ): Boolean = oldItem == newItem
        }
    }
}
