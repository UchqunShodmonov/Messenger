package uz.uchqun.telegramclone.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACT = Manifest.permission.READ_CONTACTS
const val RECORD_AUDIO  =Manifest.permission.RECORD_AUDIO
const val REQUEST_CODE = 200
const val WRITE_FILES = Manifest.permission.WRITE_EXTERNAL_STORAGE

fun checkPermision(permission: String): Boolean {

    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), REQUEST_CODE)
        return false
    } else return true
}

