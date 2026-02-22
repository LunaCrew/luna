package lunacrew.luna.auth

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import lunacrew.luna.R

class Authentication(val register: ActivityResultLauncher<Intent>) {
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.AnonymousBuilder().build(),
    )

    private val signInIntent = AuthUI
        .getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setLogo(R.mipmap.ic_launcher)
        .setTheme(R.style.Theme_Luna)
        .setTosAndPrivacyPolicyUrls("tos", "privacy")
        .build()

    fun launchSignIn() = register.launch(signInIntent)

    fun signOut(context: Context) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                // ...
            }
    }

    fun deleteAccount(context: Context) {
        AuthUI.getInstance()
            .delete(context)
            .addOnCompleteListener {
                // ...
            }
    }

    companion object {
        fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
            val response = result.idpResponse
            if (result.resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
                Log.d("SignIn", "Success => $user")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("SignIn", "Fail => ${response?.error}")
            }
        }
    }
}

