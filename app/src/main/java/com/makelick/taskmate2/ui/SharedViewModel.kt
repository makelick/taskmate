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
import com.makelick.taskmate2.model.BoardCreation
import com.makelick.taskmate2.model.Issue
import com.makelick.taskmate2.model.IssueCreation
import com.makelick.taskmate2.model.Status
import com.makelick.taskmate2.model.User
import com.makelick.taskmate2.network.TaskmateApi
import com.makelick.taskmate2.ui.signin.AuthConstants
import kotlinx.coroutines.launch

class SharedViewModel(activity: MainActivity) : ViewModel() {

    private val token = activity.token

    private val _boardsList = MutableLiveData<List<Board>>()
    val boardsList: LiveData<List<Board>> = _boardsList

    private val _currentBoard = MutableLiveData<Board?>(null)
    val currentBoard: LiveData<Board?> = _currentBoard

    private val _currentIssue = MutableLiveData<Issue?>(null)
    val currentIssue: LiveData<Issue?> = _currentIssue

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun setCurrentBoard(board: Board?) {
        _currentBoard.value = board
    }

    fun setCurrentIssue(issue: Issue?) {
        _currentIssue.value = issue
    }

    fun createIssue(title: String, description: String, status: Status) {
        val newIssue = IssueCreation(title, description, status)
        viewModelScope.launch {
            TaskmateApi.retrofitService.createIssue("Bearer_$token", currentBoard.value!!.id, newIssue)
        }
    }

    fun updateIssue(title: String, description: String, status: Status) {
        viewModelScope.launch {
            TaskmateApi.retrofitService.updateIssue(
                "Bearer_$token",
                currentBoard.value!!.id,
                IssueCreation(title, description, status)
            )
            _currentIssue.value = null
        }
    }

    fun getFullBoard(boardId: Int) {
        viewModelScope.launch {
            val board = TaskmateApi.retrofitService.getFullBoard("Bearer_$token", boardId)
            _currentBoard.value = board
        }
    }

    fun createBoard(name: String, imageUrl: String) {
        val newBoard = BoardCreation(name, imageUrl)
        viewModelScope.launch {
            TaskmateApi.retrofitService.createBoard("Bearer_$token", newBoard)
        }
    }

    fun updateBoard(id: Int, name: String, imageUrl: String) {
        viewModelScope.launch {
            TaskmateApi.retrofitService.updateBoard(
                "Bearer_$token",
                id,
                BoardCreation(name, imageUrl)
            )
            _currentBoard.value = null
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val jwt = JWT(token)
            val id = jwt.getClaim("userId").asString()!!
            _user.value = TaskmateApi.retrofitService.getUser("Bearer_$token", id)

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

    fun deleteBoard() {
        viewModelScope.launch {
            TaskmateApi.retrofitService.deleteBoard("Bearer_$token", _currentBoard.value!!.id)
            _currentBoard.value = null
        }
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