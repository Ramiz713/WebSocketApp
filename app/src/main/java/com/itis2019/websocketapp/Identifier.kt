package com.itis2019.websocketapp

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID.randomUUID

const val PREF_WEBSOCKET_APP = "PREF_WEBSOCKET"
const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
const val PREF_USER_NAME = "PREF_USER_NAME"

object Identifier {

    private lateinit var uniqueID: String
    private lateinit var sharedPrefs: SharedPreferences

    @JvmStatic
    @Synchronized
    fun getUniqueId(context: Context): String {
        if (!::uniqueID.isInitialized) {
            checkSharedPrefsIsInitialized(context)
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null) ?: ""
            if (uniqueID.isEmpty()) {
                uniqueID = randomUUID().toString()
                sharedPrefs.edit()
                    .putString(PREF_UNIQUE_ID, uniqueID)
                    .apply()
            }
        }
        return uniqueID
    }

    @JvmStatic
    @Synchronized
    fun getUserName(context: Context): String? {
        checkSharedPrefsIsInitialized(context)
        return sharedPrefs.getString(PREF_USER_NAME, null) ?: ""
    }

    fun putUserName(context: Context, userName: String) {
        checkSharedPrefsIsInitialized(context)
        sharedPrefs.edit()
            .putString(PREF_USER_NAME, userName)
            .apply()
    }

    private fun checkSharedPrefsIsInitialized(context: Context) {
        if (!::sharedPrefs.isInitialized)
            sharedPrefs = context.getSharedPreferences(PREF_WEBSOCKET_APP, Context.MODE_PRIVATE)
    }
}
