package com.example.mylocation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mylocation.data.LocationRepository
import com.example.mylocation.ui.home.LocationUIState



class HomeViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<LocationUIState>()
    val uiState: LiveData<LocationUIState> = _uiState

    fun loadLocation() {
        repository.getLastLocation { location ->
            _uiState.postValue(
                if (location != null) {
                    LocationUIState(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                } else {
                    LocationUIState(message = "Location not available")
                }
            )
        }
    }

    fun onPermissionDenied() {
        _uiState.value = LocationUIState(message = "Permission denied")
    }
}

