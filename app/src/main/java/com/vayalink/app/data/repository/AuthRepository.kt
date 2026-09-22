package com.vayalink.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vayalink.app.data.model.User
import com.vayalink.app.util.Resource
import kotlinx.coroutines.tasks.await

/**
 * Wraps the Firebase Authentication SDK (FR1, FR3, FR4, FR5) and Firestore.
 * Firebase Auth stores and salts/hashes the password server-side (FR2) - the
 * app never sees or stores a raw or hashed password itself.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentUser get() = auth.currentUser

    suspend fun register(email: String, password: String, displayName: String): Resource<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Registration failed: no UID returned")

            val user = User(uid = uid, email = email, displayName = displayName)
            firestore.collection("users").document(uid).set(user).await()

            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Registration failed")
        }
    }

    suspend fun login(email: String, password: String): Resource<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Login failed: no UID returned")
            Resource.Success(uid)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Invalid email or password")
        }
    }

    fun logout() = auth.signOut()

    suspend fun updatePassword(newPassword: String): Resource<Unit> {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not update password")
        }
    }

    suspend fun fetchProfile(uid: String): Resource<User> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java) ?: return Resource.Error("Profile not found")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load profile")
        }
    }

    suspend fun updateProfile(user: User): Resource<Unit> {
        return try {
            firestore.collection("users").document(user.uid).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not update profile")
        }
    }
}
