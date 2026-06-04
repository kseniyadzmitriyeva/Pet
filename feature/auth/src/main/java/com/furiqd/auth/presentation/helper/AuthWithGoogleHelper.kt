package com.furiqd.auth.presentation.helper

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.furiqd.auth.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class AuthWithGoogleHelper @Inject constructor() {

    sealed interface AuthUiResult {
        data class GoogleSuccess(val idToken: String) : AuthUiResult
        data class Failure(val error: Throwable) : AuthUiResult
    }

    suspend fun launchAuthUi(context: Context): AuthUiResult {
        val credentialManager = CredentialManager.create(context)

        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isEmpty()){
            return AuthUiResult.Failure(Exception("GOOGLE_WEB_CLIENT_ID isn't found"))
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential

            when {
                credential is GoogleIdTokenCredential -> {
                    AuthUiResult.GoogleSuccess(credential.idToken)
                }
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    AuthUiResult.GoogleSuccess(googleIdTokenCredential.idToken)
                }
                else -> {
                    AuthUiResult.Failure(Exception("Неизвестный тип данных: ${credential.type} (${credential::class.java.simpleName})"))
                }
            }
        } catch (e: GetCredentialException) {
            AuthUiResult.Failure(e)
        }
    }
}