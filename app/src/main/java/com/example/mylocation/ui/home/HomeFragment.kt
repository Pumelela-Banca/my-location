package com.example.mylocation.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mylocation.R
import com.example.mylocation.data.LocationRepository


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var longitudeLocation: TextView
    private lateinit var latitudeLocation: TextView



    private val viewModel: HomeViewModel by viewModels {
        // Pass the LocationRepository to the HomeViewModelFactory
        HomeViewModelFactory(
            LocationRepository(requireContext().applicationContext)
        )
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            granted ->
            if (granted) {
                viewModel.loadLocation()
            } else {
                viewModel.onPermissionDenied()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        longitudeLocation = view.findViewById(R.id.longitude)
        latitudeLocation = view.findViewById(R.id.latitude)

        val btnLocation = view.findViewById<Button>(R.id.button)

        btnLocation.setOnClickListener {
            requestLocation()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when {
                state.message != null -> {
                    longitudeLocation.text = state.message
                    latitudeLocation.text = state.message
                }
                state.latitude != null && state.longitude != null -> {
                    longitudeLocation.text = "Longitude: ${state.longitude}"
                    latitudeLocation.text = "Latitude: ${state.latitude}"
                }
            }
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.loadLocation()
        }
    }
}
