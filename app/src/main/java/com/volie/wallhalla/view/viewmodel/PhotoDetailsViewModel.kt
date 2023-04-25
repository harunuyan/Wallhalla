package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun savePhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCuratedResponse(photo)
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCuratedResponse(photo)

        }
    }
}