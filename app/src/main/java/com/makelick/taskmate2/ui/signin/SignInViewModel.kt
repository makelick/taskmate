package com.makelick.taskmate2.ui.signin

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import org.json.JSONException
import java.security.MessageDigest
import java.security.SecureRandom

class SignInViewModel: ViewModel() {
    private var authState = MutableLiveData(AuthState())
    private var jwt = MutableLiveData<JWT>()
    private lateinit var authorizationService: AuthorizationService
    private lateinit var authServiceConfig: AuthorizationServiceConfiguration


    val jwtLiveData : LiveData<JWT> = jwt
    val authStateLiveData : LiveData<AuthState> = authState

    fun attemptAuthorization(): Intent {
        // code verifier
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier = Base64.encodeToString(bytes, encoding)

        // code challenge
        val digest = MessageDigest.getInstance(AuthConstants.MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        val codeChallenge = Base64.encodeToString(hash, encoding)

        // authorization request
        val builder = AuthorizationRequest.Builder(
            authServiceConfig,
            AuthConstants.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(AuthConstants.URL_AUTH_REDIRECT)
        )
            .setCodeVerifier(
                codeVerifier,
                codeChallenge,
                AuthConstants.CODE_VERIFIER_CHALLENGE_METHOD
            )

        // scopes
        builder.setScopes(
            AuthConstants.SCOPE_PROFILE,
            AuthConstants.SCOPE_EMAIL,
            AuthConstants.SCOPE_OPENID
        )

        val request = builder.build()

        return authorizationService.getAuthorizationRequestIntent(request)

    }

    fun exchangeAuthorizationCode(authorizationResponse: AuthorizationResponse) {
        val tokenExchangeRequest = authorizationResponse.createTokenExchangeRequest()
        authorizationService.performTokenRequest(tokenExchangeRequest) { response, exception ->
            if (exception != null) {
                authState.value = AuthState()
            } else {
                if (response != null) {
                    authState.value?.update(response, exception)
                    jwt.value = JWT(response.idToken!!)
                }
            }
        }
    }

    fun setAuthState(response: AuthorizationResponse?, exception: AuthorizationException?) {
        this.authState.value = AuthState(response, exception)
    }

    fun initAuthService(context: Context) {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                    VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
                )
            ).build()

        authorizationService = AuthorizationService(
            context,
            appAuthConfiguration
        )
    }

    fun initAuthServiceConfig() {
        authServiceConfig = AuthorizationServiceConfiguration(
            Uri.parse(AuthConstants.URL_AUTHORIZATION),
            Uri.parse(AuthConstants.URL_TOKEN_EXCHANGE),
            null,
            Uri.parse(AuthConstants.URL_LOGOUT)
        )
    }

    fun persistState(context: Context) {
        context.getSharedPreferences(AuthConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(AuthConstants.AUTH_STATE, authState.value?.jsonSerializeString())
            .apply()
    }

    fun restoreState(application: Application) {
        val jsonString = application
            .getSharedPreferences(AuthConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(AuthConstants.AUTH_STATE, null)

        if (jsonString != null && !TextUtils.isEmpty(jsonString)) {
            try {
                authState.value = AuthState.jsonDeserialize(jsonString)

                if (!TextUtils.isEmpty(authState.value?.idToken)) {
                    jwt.value = JWT(authState.value?.idToken!!)
                }

            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }

        }
    }
}