package com.volie.wallhalla.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.User
import com.volie.wallhalla.util.Constant.USERS
import com.volie.wallhalla.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {


    private val _popUpNotification = MutableLiveData<Event<String>>()
    val popUpNotification: LiveData<Event<String>> = _popUpNotification

    private val _inProgress = MutableLiveData<Boolean>(false)
    val inProgress: LiveData<Boolean> = _inProgress

    private val _signedIn = MutableLiveData<Boolean>(false)
    val signedIn: LiveData<Boolean> = _signedIn

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    init {
        val currentUser = auth.currentUser
        _signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = User(
                        uid = account.id.toString(),
                        fullName = account.displayName.toString(),
                        username = "@${account.displayName.toString().replace(" ", "")}",
                        email = account.email.toString(),
                        profilePic = account.photoUrl.toString()
                    )
                } else {
                    handleException(
                        exception = task.exception,
                        customMessage = "Failed Google sign in"
                    )
                }
            }
    }

    fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
        if (account != null) {
            signInWithGoogle(account)
        }
    }

    fun onSignup(fullName: String, email: String, password: String) {
        _inProgress.postValue(true)
        db.collection(USERS).whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    handleException(customMessage = "Username already exist")
                    _inProgress.postValue(false)
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _inProgress.postValue(true)
                                val user = User(
                                    uid = auth.currentUser?.uid.toString(),
                                    fullName = fullName,
                                    username = "@${fullName.replace(" ", "")}",
                                    email = email,
                                    profilePic = R.drawable.ic_user.toString() //TODO change this if it is not working
                                )
                                createOrUpdateProfile(user)
                                _user.postValue(user)
                            } else {
                                handleException(
                                    exception = task.exception,
                                    customMessage = "Signup failed"
                                )
                            }
                            _inProgress.postValue(false)
                        }
                }
            }
    }

    fun onLogin(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please fill in the fields")
            return
        }
        _inProgress.postValue(true)

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _signedIn.postValue(true)
                _inProgress.postValue(false)
                auth.currentUser?.uid?.let { uid ->
                    getUserData(uid = uid)
                }
                onSuccess()
            } else {
                handleException(exception = task.exception, customMessage = "Login failed")
                _inProgress.postValue(false)
            }
        }.addOnFailureListener { exception ->
            handleException(exception = exception, customMessage = "Login failed")
            _inProgress.postValue(false)
        }
    }

    private fun createOrUpdateProfile(user: User) {
        val uid = auth.currentUser?.uid
        uid?.let { uid ->
            _inProgress.postValue(true)
            db.collection(USERS).document(uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    documentSnapshot.reference.update(user.toMap()).addOnSuccessListener {
                        _user.postValue(user)
                        _inProgress.postValue(false)
                    }.addOnFailureListener { exception ->
                        handleException(exception = exception, customMessage = "Cannot update user")
                        _inProgress.postValue(false)
                    }
                } else {
                    db.collection(USERS).document(uid).set(user)
                    getUserData(uid = uid)
                    _inProgress.postValue(false)
                }
                _inProgress.postValue(false)
            }.addOnFailureListener { exception ->
                handleException(exception = exception, customMessage = "Cannot create user")
                _inProgress.postValue(false)
            }
        }
    }

    private fun getUserData(uid: String) {
        _inProgress.postValue(true)
        db.collection(USERS).document(uid).get().addOnSuccessListener {
            val user = it.toObject<User>()
            _user.postValue(user!!)
            _inProgress.postValue(false)
        }.addOnFailureListener { exception ->
            handleException(exception = exception, "Cannot retrieve user data")
            _inProgress.postValue(false)
        }
    }

    fun onLogOut() {
        _popUpNotification.postValue(Event(content = "Logged out: ${auth.currentUser?.displayName}"))
        auth.signOut()
        _user.postValue(null)
    }

    fun signInGuest() {
        val guest = User(
            uid = UUID.randomUUID().toString(),
            fullName = "Guest",
            profilePic = ""
        )
        _user.postValue(guest)
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage $errorMsg"
        _popUpNotification.postValue(Event(message))
    }
}