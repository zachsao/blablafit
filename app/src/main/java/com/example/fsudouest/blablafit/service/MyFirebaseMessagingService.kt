package com.example.fsudouest.blablafit.service

import android.util.Log
import com.example.fsudouest.blablafit.utils.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.NullPointerException


class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        Log.d("MessagingService", "token refreshed")
        if (FirebaseAuth.getInstance().currentUser != null) {
            addTokenToFirestore(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            // TODO : show notification
            Log.d("FCM", "${remoteMessage.data}")
        }
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?){
            newRegistrationToken?.let {
                FirestoreUtil.setFCMRegistrationToken(it)
            } ?: throw NullPointerException("FCM token is null")
        }
    }


}