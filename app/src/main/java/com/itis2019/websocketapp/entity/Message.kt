package com.itis2019.websocketapp.entity

sealed class MessageDataItem{
    abstract val id: Int
}

data class Message(
    val id: Int,
    val user: String,
    val message: String
)

data class UserMessage(val message: Message): MessageDataItem(){
    override val id = message.id
}
data class ChatMessage(val message: Message): MessageDataItem(){
    override val id = message.id
}
