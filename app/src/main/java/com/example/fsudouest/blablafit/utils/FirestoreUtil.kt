package com.example.fsudouest.blablafit.utils

import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUserId = authInstance.currentUser?.uid ?: ""
    private val currentUserDocRef = firestoreInstance.collection("users")
            .document(currentUserId)


    fun getRegistrationToken(onComplete: (String?) -> Unit) {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.w("FirestoreUtils, getInstanceId failed:  $task{.exception}")
                        return@addOnCompleteListener
                    }
                    val token = task.result?.token
                    onComplete(token)
                }
    }

    fun setFCMRegistrationToken(registrationToken: String){
        currentUserDocRef.update("registrationToken", registrationToken)
    }

    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java) ?: User()
            onComplete(user)
        }
    }
}