package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.CollectionResponse
import com.volie.wallhalla.data.repo.Repository
import com.volie.wallhalla.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _collections = MutableLiveData<Resource<CollectionResponse>>()
    val collections: LiveData<Resource<CollectionResponse>> = _collections

    fun getFeaturedCollections(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _collections.postValue(Resource.loading(null))
            _collections.postValue(repository.getFeaturedCollections(page))
        }
    }
}