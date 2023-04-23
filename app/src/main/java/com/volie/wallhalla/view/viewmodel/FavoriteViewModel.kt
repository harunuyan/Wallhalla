package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _savedPhotos = MutableLiveData<List<Photo>>()
    val savedPhotos: LiveData<List<Photo>> = _savedPhotos

    fun getSavedPhotos() {
        viewModelScope.launch {
            val db = repository.getWallpapersFromDb()
            _savedPhotos.postValue(db)
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            repository.deleteCuratedResponse(photo)
        }
    }
}