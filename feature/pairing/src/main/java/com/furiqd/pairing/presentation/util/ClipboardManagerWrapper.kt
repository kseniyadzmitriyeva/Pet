package com.furiqd.pairing.presentation.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ClipboardManagerWrapper {
    fun copyToClipboard(text: String)
}

class AndroidClipboardManagerWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) : ClipboardManagerWrapper {
    override fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Invite Code", text)
        clipboard.setPrimaryClip(clip)
    }
}