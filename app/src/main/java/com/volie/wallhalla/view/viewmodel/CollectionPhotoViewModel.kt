package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.CollectionMediaResponse
import com.volie.wallhalla.data.repo.Repository
import com.volie.wallhalla.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionPhotoViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _photos = MutableLiveData<Resource<CollectionMediaResponse>>()
    val photos: LiveData<Resource<CollectionMediaResponse>> = _photos

    fun getCollectionPhotos(id: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _photos.postValue(Resource.loading(null))
            _photos.postValue(repository.getCollectionPhotos(id, page))
        }
    }
}