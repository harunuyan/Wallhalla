package com.volie.wallhalla.view.viewmodel.collection_vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.CollectionMediaResponse
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.data.repo.Repository
import com.volie.wallhalla.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionVideoViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _videosPictures = MutableLiveData<Resource<CollectionMediaResponse>>()
    val videosPictures: LiveData<Resource<CollectionMediaResponse>> = _videosPictures

    fun getVideosPictures(id: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _videosPictures.postValue(Resource.loading(null))
            _videosPictures.postValue(repository.getCollectionResults(id, page, "videos"))
        }
    }

    fun saveVideo(video: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPhoto(video)
        }
    }

    fun deleteVideo(video: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePhoto(video)
        }
    }
}