package com.akv.akvandroidapprelease.ui


import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.util.Constants
import java.util.*

abstract class BaseLocationActivity: BaseActivity(),LocationListener,
    DataStateChangeListener,
    UICommunicationListener
{

    protected var isGPS = false
    protected var isNetwork = false
    protected lateinit var locationManager: LocationManager
    protected lateinit var providers: List<String>
    private var loc: Location? = null
    protected var permissions = ArrayList<String>()
    protected lateinit var permissionsToRequest: ArrayList<String>
    protected var canGetLocation = true
    private var check = false
    private var permissionsRejected = ArrayList<String>()

    fun getLocation() {
        var location: Location? = null
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            // getting GPS status
            isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // getting network status
            isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPS && !isNetwork) { // no network provider is enabled
                Log.d(TAG,"no network provider is enabled")
            } else {
                canGetLocation = true
                if (isNetwork) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        Constants.MIN_TIME_BW_UPDATES,
                        Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    for (provider in providers) {
                        val l =
                            locationManager.getLastKnownLocation(provider) ?: continue
                        if (location == null || l.accuracy < location.accuracy) {
                            location = l
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPS) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            Constants.MIN_TIME_BW_UPDATES,
                            Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        for (provider in providers) {
                            val l =
                                locationManager.getLastKnownLocation(provider)
                                    ?: continue
                            if (location == null || l.accuracy < location.accuracy) {
                                location = l
                            }
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        if (location != null) {
            loc = location
            sessionManager.filterLocation(location)
            check = true

        } else if (!isNetwork && isGPS) {
            showSettingsAlert(2)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel(getString(R.string.permission_text),
                                DialogInterface.OnClickListener { _, _ ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            permissionsRejected.toTypedArray(),
                                            Constants.ALL_PERMISSIONS_RESULT
                                        )
                                    }
                                },
                                DialogInterface.OnClickListener { _, _ ->
                                    onPermissionDeclined()
                                },this)
                            return
                        }
                    }
                } else {
                    canGetLocation = true
                    getLocation()
                }
            }
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 33 && resultCode == 0) {
            isGPS = true
            providers = locationManager.getProviders(true)
            isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            getLocation()
        }
    }

    protected open fun onPermissionDeclined() = Unit

    override fun onLocationChanged(location: Location) {
        sessionManager.filterLocation(location)
    }

    override fun onStatusChanged(s: String?, i: Int, bundle: Bundle?) {}

    override fun onProviderEnabled(s: String?) {
        getLocation()
    }

    override fun onProviderDisabled(s: String?) {
        locationManager.removeUpdates(this)
    }
}











