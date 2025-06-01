package com.example.scstrade.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.scstrade.R
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

object GoogleSignInUtils {

    private const val TAG = "GoogleSignInUtils"
    const val RC_SIGN_IN = 9001

    private lateinit var googleSignInClient: GoogleSignInClient

    /**
     * Initialize GoogleSignInClient
     */
    fun initGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient
    }

    /**
     * Launch Sign-In Intent
     */
    fun launchSignIn(activity: Activity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Handle Sign-In Result in onActivityResult
     */
    fun handleSignInResult(
        data: Intent?,
        activity: Activity,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, activity, onSuccess, onFailure)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            onFailure(e)
        }
    }

    /**
     * Firebase Authentication with Google
     */
    private fun firebaseAuthWithGoogle(
        idToken: String,
        activity: Activity,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d(TAG, "signInWithCredential:success")
                    onSuccess(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    onFailure(task.exception)
                }
            }
    }

    /**
     * Sign out
     */
    fun signOut(context: Context, onComplete: () -> Unit = {}) {
        googleSignInClient.signOut().addOnCompleteListener {
            FirebaseAuth.getInstance().signOut()
            onComplete()
        }
    }
}
