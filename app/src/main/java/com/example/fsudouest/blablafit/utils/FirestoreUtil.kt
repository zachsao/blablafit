package com.example.fsudouest.blablafit.utils

import android.util.Log
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val authInstance: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val currentUserId = authInstance.currentUser?.uid ?: ""
    private val currentUserDocRef = firestoreInstance.collection("users")
            .document(currentUserId)


    fun getRegistrationToken(onComplete: (String) -> Unit){
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FirestoreUtils", "getInstanceId failed", task.exception)
                        return@addOnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result?.token ?: ""
                    onComplete(token)
                }
    }

    fun getFCMRegistrationTokens(onComplete: (MutableList<String>) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java) ?: User()
            onComplete(user.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>){
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
}