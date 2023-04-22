package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.model.CuratedResponse
import com.volie.wallhalla.data.repo.Repository
import com.volie.wallhalla.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _wallpapers = MutableLiveData<Resource<CuratedResponse>>()
    val wallpapers: LiveData<Resource<CuratedResponse>> = _wallpapers

    fun getWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            _wallpapers.postValue(Resource.loading(null))
            _wallpapers.postValue(repository.getWallpapers())
        }
    }
}