package com.itis2019.websocketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.itis2019.websocketapp.api.LoginRequest
import com.itis2019.websocketapp.api.TechnoChatApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: TechnoChatApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        apiService = TechnoChatApiService.create()
        val uniqueId = Identifier.getUniqueId(this)
        login_btn.setOnClickListener {
            val username = username_ed.text.toString()
            if (username.isEmpty())
                username_input_layout.error = "Cannot be empty"
            else login(LoginRequest(username, uniqueId))
        }
    }

    @Suppress("CheckResult")
    private fun login(loginRequest: LoginRequest) {
        apiService.login(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.errors.isNullOrEmpty())
                    Toast.makeText(this, it.errors, Toast.LENGTH_SHORT).show()
                Identifier.putUserName(this, loginRequest.username)
                startChat()
            }, {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            })
    }

    private fun startChat() {
        startActivity(Intent(this, ChatActivity::class.java))
        finish()
    }
}
