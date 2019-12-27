package com.example.fsudouest.blablafit.service

import com.example.fsudouest.blablafit.utils.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        Timber.d("MessagingService, token refreshed : $token")
        if (FirebaseAuth.getInstance().currentUser != null) {
            addTokenToFirestore(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            Timber.d("FCM, ${remoteMessage.data}")
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