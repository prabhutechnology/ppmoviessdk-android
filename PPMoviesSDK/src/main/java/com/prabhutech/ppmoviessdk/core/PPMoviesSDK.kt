package com.prabhutech.ppmoviessdk.core

import android.content.Context
import android.content.Intent
import com.prabhutech.ppmoviessdk.core.constant.SDKConstant.PASSWORD_KEY
import com.prabhutech.ppmoviessdk.core.constant.SDKConstant.USERNAME_KEY
import com.prabhutech.ppmoviessdk.view.MainActivity

class PPMoviesSDK(
    private val context: Context,
    username: String,
    password: String
) {
    private val username: String
    private val password: String

    init {
        this.username = username
        this.password = password
        openActivity()
    }

    private fun openActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(USERNAME_KEY, username)
        intent.putExtra(PASSWORD_KEY, password)
        context.startActivity(intent)
    }
}
