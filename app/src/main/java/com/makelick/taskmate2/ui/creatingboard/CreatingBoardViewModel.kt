package com.makelick.taskmate2.ui.creatingboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makelick.taskmate2.network.TaskmateApi
import com.makelick.taskmate2.ui.MainActivity
import kotlinx.coroutines.launch

class CreatingBoardViewModel : ViewModel() {

    var imageUrl: String = "https://res.cloudinary.com/dtay106eo/image/upload/v1682436194/oc-gonzalez-xg8z_KhSorQ-unsplash_gfhgto.webp"

    fun saveBoard(name: String, activity: MainActivity) {
        val token = activity.token
        val board = mapOf("name" to name, "imageUrl" to imageUrl)
        viewModelScope.launch{
            TaskmateApi.retrofitService.createBoard("Bearer_$token", board)
        }
    }
}