package com.volie.wallhalla.view.fragment.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.wallhalla.data.local.data_store.DataStoreRepository
import com.volie.wallhalla.data.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val selectedTheme: Flow<Theme> = dataStoreRepository.selectedThemeFlow

    fun saveTheme(theme: Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveThemeOption(theme)
        }
    }
}