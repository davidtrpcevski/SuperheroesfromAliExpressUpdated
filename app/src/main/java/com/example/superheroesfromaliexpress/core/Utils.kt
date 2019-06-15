package com.example.superheroesfromaliexpress.core

import android.app.Activity
import com.crazylegend.kotlinextensions.context.shortToast
import com.example.superheroesfromaliexpress.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

fun Activity.checkSinglePermission(permissionName: String, action: () -> Unit) {
    Dexter.withActivity(this)
            .withPermission(permissionName)
            .withListener(permissionListenerDSL({
                action()
            }, { _, token ->
                token?.continuePermissionRequest()
            }, { response ->
                if (response.isPermanentlyDenied) {
                    shortToast(getString(R.string.permission_rationale))
                }
                finish()
            })).check()
}


private fun permissionListenerDSL(
        permissionGranted: (response: PermissionGrantedResponse) -> Unit = { _ -> },
        permissionRationale: (permission: PermissionRequest?, token: PermissionToken?) -> Unit = { _, _ -> },
        permissionDenied: (response: PermissionDeniedResponse) -> Unit = { _ -> }
): PermissionListener {

    return object : PermissionListener {
        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
            response?.apply {
                permissionGranted(response)
            }
        }

        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
            permissionRationale(permission, token)
        }

        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            response?.apply {
                permissionDenied(this)
            }
        }
    }

}