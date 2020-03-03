package com.example.akvandroidapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.akvandroidapp.R
import java.util.*


fun Activity.displayToast(@StringRes message:Int){
    Toast.makeText(this, message,Toast.LENGTH_LONG).show()
}

fun Activity.displayToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Activity.displaySuccessDialog(message: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(errorMessage: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = errorMessage)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayInfoDialog(message: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.areYouSureDialog(message: String, callback: AreYouSureCallback){
    MaterialDialog(this)
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
            }
        }
}

fun Activity.showSettingsAlert(cont: Int){
    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
    var title = getString(R.string.gps_question_click)
    if (cont == 1) {
        title = getString(R.string.gps_not_active)
        alertDialog.setMessage(R.string.gps_question)
    } else if (cont == 2) {
        title = getString(R.string.gps_impossible)
        alertDialog.setMessage(R.string.gps_try_network)
    }
    alertDialog.setTitle(title)
    alertDialog.setPositiveButton(R.string.yes
    ) { _, _ ->
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, 33)
    }
    alertDialog.setNegativeButton(R.string.no
    ) { dialog, _ -> dialog.cancel() }
    alertDialog.show()
}

fun Activity.findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
    val result: ArrayList<String> = ArrayList()
    for (perm in wanted) {
        if (!hasPermission(perm)) {
            result.add(perm)
        }
    }
    return result

}

fun Activity.hasPermission(permission: String): Boolean {
    if (canAskPermission()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) === android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    return true
}

fun canAskPermission(): Boolean {
    return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
}

fun getLastLocation(locationManager: LocationManager) {
    try {
        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria, false)
        if (provider != null) {
            val location = locationManager.getLastKnownLocation(provider)
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

fun showMessageOKCancel(
    message: String,
    okListener: DialogInterface.OnClickListener,
    cancelListener: DialogInterface.OnClickListener,
    context: Context
) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(R.string.text_ok, okListener)
        .setNegativeButton(R.string.cancel, cancelListener)
        .create()
        .show()
}



interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}








