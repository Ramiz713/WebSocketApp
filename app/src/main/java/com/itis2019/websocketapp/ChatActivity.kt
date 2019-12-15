package com.itis2019.websocketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itis2019.websocketapp.adapter.MessagesAdapter
import com.itis2019.websocketapp.api.TechnoResponse
import com.itis2019.websocketapp.entity.ChatMessage
import com.itis2019.websocketapp.entity.MessageDataItem
import com.itis2019.websocketapp.entity.MessagesSection
import com.itis2019.websocketapp.entity.UserMessage
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.*

class ChatActivity : AppCompatActivity() {

    companion object {
        private val NORMAL_CLOSURE_STATUS = 1000
    }

    private val client = OkHttpClient()
    private var connected = false

    private lateinit var ws: WebSocket
    private lateinit var webSocketListener: WebSocketListener

    private var isMessageSend = false
    private var lastMessageId = 0
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chat_messages_rv.adapter = MessagesAdapter()

        val uniqueId = Identifier.getUniqueId(this)
        userName = Identifier.getUserName(this)
        if (userName.isNullOrEmpty()) {
            logout()
            return
        }

        send_message_iv.setOnClickListener {
            val messageText = message_ed.text.toString()
            if (messageText.isNotEmpty()) sendMessage(messageText)
            else message_ed.error = "Cannot be empty"
        }

        webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("{ \"device_id\" : \"$uniqueId\" }")
            }

            override fun onMessage(webSocket: WebSocket?, text: String?) {
                if (connected) {
                    connected = false
                    val messages = formatMessagesFromJson(text)
                    runOnUiThread {
                        (chat_messages_rv.adapter as MessagesAdapter).submitList(messages)
                    }
                } else handleResponse(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                runOnUiThread { showError(t.localizedMessage) }
            }
        }

        val request =
            Request.Builder().url("wss://backend-chat.cloud.technokratos.com/chat").build()
        ws = client.newWebSocket(request, webSocketListener)
        client.dispatcher().executorService().shutdown()
    }

    private fun formatMessagesFromJson(text: String?): List<MessageDataItem> {
        val messagesSections: MessagesSection =
            Gson().fromJson(text, object : TypeToken<MessagesSection>() {}.type)
        val messages = messagesSections.items.map { message ->
            if (message.user == userName) UserMessage(message)
            else ChatMessage(message)
        }.sortedBy { it.id }
        lastMessageId = messages.last().id
        return messages
    }

    private fun handleResponse(text: String?) {
        val response: TechnoResponse =
            Gson().fromJson(text, object : TypeToken<TechnoResponse>() {}.type)
        if (!response.errors.isNullOrEmpty())
            runOnUiThread { showError(response.errors) }
        if (isMessageSend) {
            isMessageSend = false
            getLastMessages()
        } else getLastMessages()
    }

    private fun showError(errorMessage: String?) {
        error_tv.visibility = View.VISIBLE
        error_tv.text = "Error $errorMessage. Please restart app"

        chat_messages_rv.visibility = View.GONE
        message_ed.isEnabled = false
        send_message_iv.isEnabled = false
    }

    private fun getLastMessages() {
        connected = true
        ws.send("{ \"history\": { \"limit\": 100 , \"offset\": $lastMessageId } }")
    }

    private fun sendMessage(messageText: String) {
        message_ed.text.clear()
        ws.send("{ \"message\": \"$messageText\" }")
        isMessageSend = true
    }

    private fun logout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
