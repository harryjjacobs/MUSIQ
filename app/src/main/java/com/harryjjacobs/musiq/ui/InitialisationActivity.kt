package com.harryjjacobs.musiq.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.harryjjacobs.musiq.App
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.CLIENT_ID
import com.harryjjacobs.musiq.spotify.SpotifyAuthHelper.REDIRECT_URI
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class InitialisationActivity : AppCompatActivity() {
    private val scopes = arrayOf(
        "user-read-email",
        "app-remote-control",
        "playlist-read-private",
        "playlist-read-collaborative",
        "user-modify-playback-state",
        "user-library-read",
        "user-top-read",
        "user-read-recently-played",
        "user-read-playback-position"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState)
        val token: String? = SpotifyAuthHelper.getToken(this)
        if (token == null) {
            login();
        } else {
            startMainActivity()
        }
    }

    private fun login() {
        val request =
            getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    private fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest? {
        return AuthorizationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
            .setShowDialog(false)
            .setScopes(scopes)
            .build()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    SpotifyAuthHelper.setToken(this, response.accessToken, response.expiresIn, TimeUnit.SECONDS);
                    startMainActivity();
                }
                AuthorizationResponse.Type.ERROR -> {
                    logError("Auth error: " + response.error);
                }
                else -> {
                    logError("Auth result: " + response.type);
                }
            }
        }
    }

    private fun startMainActivity() {
        App.spotifyRepository.initialiseApi(applicationContext)
        val intent = Intent(this, MainActivity::class.java)
        GlobalScope.launch {
            delay(LAUNCH_DELAY)
            startActivity(intent)
            finish()    // exit this activity
        }
    }

    private fun logError(msg: String) {
        Toast.makeText(this, "Error: $msg", Toast.LENGTH_SHORT).show()
        Log.e(TAG, msg)
    }

    private fun logMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        Log.d(TAG, msg)
    }

    companion object {
        private val TAG = InitialisationActivity::class.java.simpleName
        private const val LAUNCH_DELAY = 1500L; // milliseconds
    }
}
