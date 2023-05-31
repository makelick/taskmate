package com.makelick.taskmate2.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.makelick.taskmate2.model.Board
import com.makelick.taskmate2.model.User
import com.makelick.taskmate2.network.TaskmateApi
import com.makelick.taskmate2.ui.signin.AuthConstants
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState

class SharedViewModel(activity: MainActivity) : ViewModel() {

    private val token = activity.token

    private val _boardsList = MutableLiveData<List<Board>>()
    val boardsList: LiveData<List<Board>> = _boardsList

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user


    fun createBoard(name: String, imageUrl: String) {
        val board = mapOf("name" to name, "imageUrl" to imageUrl)
        viewModelScope.launch {
            TaskmateApi.retrofitService.createBoard("Bearer_$token", board)
        }
    }

    fun getUser(application: Application) {
        viewModelScope.launch {
            val jsonString = application
                .getSharedPreferences(AuthConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(AuthConstants.AUTH_STATE, "")!!

            val jwt = JWT(AuthState.jsonDeserialize(jsonString).idToken!!)
//            val id = jwt.getClaim("sub").asString()!!
//            _user.value = TaskmateApi.retrofitService.getUser("Bearer_$token", id)
            _user.value = User(
                id = jwt.getClaim("sub").asString()!!,
                email = jwt.getClaim("email").asString()!!,
                profileImageUrl = jwt.getClaim("picture").asString()!!,
                firstName = jwt.getClaim("given_name").asString()!!,
                lastName = jwt.getClaim("family_name").asString()?: ""
            )
        }
    }

    fun getBoards() {
        viewModelScope.launch {
            _boardsList.value = TaskmateApi.retrofitService.getBoards("Bearer_$token")
        }
    }

    fun logout(context: Context) {
        context.getSharedPreferences(AuthConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(AuthConstants.AUTH_STATE, null)
            .apply()
    }

}

class SharedViewModelFactory(private val activity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(activity) as T
        } else throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}