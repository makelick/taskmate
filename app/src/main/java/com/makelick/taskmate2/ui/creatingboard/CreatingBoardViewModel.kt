package com.makelick.taskmate2.ui.creatingboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatingBoardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is creating board Fragment"
    }
    val text: LiveData<String> = _text
}