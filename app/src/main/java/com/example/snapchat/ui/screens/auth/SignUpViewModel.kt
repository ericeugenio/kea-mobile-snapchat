package com.example.snapchat.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapchat.data.repository.UserRepository
import com.example.snapchat.utilities.isValidEmail
import com.example.snapchat.utilities.isValidPassword
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val username
        get() = _uiState.value.username
    private val email
        get() = _uiState.value.email
    private val password
        get() = _uiState.value.password
    private val repeatPassword
        get() = _uiState.value.password

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onRepeatPasswordChange(newRepeatPassword: String) {
        _uiState.update { it.copy(repeatPassword = newRepeatPassword) }
    }

    fun onSignUp(navigateToChat: () -> Unit)  {
        if (username.isBlank()) {
            Log.e(TAG, "Username is empty.")
            return
        }

        if (!email.isValidEmail()) {
            Log.e(TAG, "Email format is invalid.")
            return
        }

        if (!password.isValidPassword()) {
            Log.e(TAG, "Password format is invalid.")
            return
        }

        if (password != repeatPassword) {
            Log.e(TAG, "Password and repeatPassword do not match.")
            return
        }

        viewModelScope.launch {
            try {
                userRepository.signUpUser(username, email, password)
                navigateToChat()
            }
            /* TODO: catch exceptions in authRepository */
            catch (e: FirebaseAuthException) {
                Log.e(TAG, "${e.errorCode }: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "SignUp"
    }
}

data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)