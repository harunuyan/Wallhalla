package com.volie.wallhalla.view.viewmodel

import androidx.lifecycle.ViewModel
import com.volie.wallhalla.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel()