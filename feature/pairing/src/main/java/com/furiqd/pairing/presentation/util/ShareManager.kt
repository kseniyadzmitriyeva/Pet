package com.furiqd.pairing.presentation.util

import android.content.Context
import android.content.Intent
import com.furiqd.pairing.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ShareManager {
    fun shareInvite(code: String, petName: String)
}

class AndroidShareManager@Inject constructor(
    @ApplicationContext private val context: Context
) : ShareManager {
    override fun shareInvite(code: String, petName: String) {
        val shareText = context.getString(R.string.share_invite_text, petName, code)
        val chooserTitle = context.getString(R.string.share_chooser_title)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(intent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}