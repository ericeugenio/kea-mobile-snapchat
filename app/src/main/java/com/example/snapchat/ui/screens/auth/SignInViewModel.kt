package com.example.snapchat.ui.screens.auth


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapchat.data.repository.AuthRepository
import com.example.snapchat.utilities.isValidEmail
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val email
        get() = _uiState.value.email
    private val password
        get() = _uiState.value.password

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun signIn(navigateToHome: () -> Unit)  {
        if (!email.isValidEmail()) {
            Log.e(TAG, "Email format is invalid.")
            return
        }

        if (password.isBlank()) {
            Log.e(TAG, "Password is empty.")
            return
        }

        viewModelScope.launch {
            try {
                authRepository.signInUser(email, password)
                navigateToHome()
            }
            /* TODO: catch exceptions in authRepository */
            catch (e: FirebaseAuthException) {
                Log.e(TAG, "${e.errorCode }: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "SignIn"
    }
}

data class SignInUiState(
    val email: String = "",
    val password: String = ""
)