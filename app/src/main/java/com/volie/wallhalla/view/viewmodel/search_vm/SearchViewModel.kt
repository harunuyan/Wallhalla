package com.volie.wallhalla.view.viewmodel.search_vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.data.model.SearchResponse
import com.volie.wallhalla.data.repo.Repository
import com.volie.wallhalla.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _searchData = MutableLiveData<Resource<SearchResponse>>()
    val searchData: LiveData<Resource<SearchResponse>> = _searchData


    fun getSearchResult(query: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchData.postValue(Resource.loading(null))
            _searchData.postValue(repository.getSearchResult(query, page))
        }
    }

    fun savePhoto(photo: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPhoto(photo)
        }
    }

    fun deletePhoto(photo: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePhoto(photo)
        }
    }
}