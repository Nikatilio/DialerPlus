package com.enzo.dialerplus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.IntentFilter
import androidx.appcompat.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class DialerPlusActivity : AppCompatActivity() {

    private val TAG = DialerPlusActivity::class.simpleName

    private val NOTIF_CHANNEL_ID = "NOTIF_CHANNEL_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        Log.d(TAG, "DialerPlusActivity onCreate")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "READ_PHONE_STATE permission is not granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 0)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "PROCESS_OUTGOING_CALLS permission is not granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS), 1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "SYSTEM_ALERT_WINDOW permission is not granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW), 2)
        }

        var callReceiver = CallReceiver()
        callReceiver.setListener {
            showAlert(it)
        }

        val filter = IntentFilter("android.intent.action.PHONE_STATE")
        this.registerReceiver(callReceiver, filter)

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChannelName1"
            val descriptionText = "ChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIF_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showAlert(phoneNumber: String?) {

        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }

        var phoneNumberFormatted = "Unknown number"

        if (!phoneNumber.isNullOrEmpty()) {
            var phoneClean = phoneNumber.replace(" ", "").substring(1, phoneNumber.length - 1)
            if (phoneClean.length == 10) {
                phoneNumberFormatted = "(" + phoneClean.substring(0, 2) + ") " + phoneClean.substring(3, 5) + "-" + phoneClean.substring(6, 9)
            } else {
                phoneNumberFormatted = phoneClean
            }
        }

        var notifBuilder = NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
            .setSmallIcon(R.drawable.alert_light_frame)
            .setContentTitle("My notification")
            .setContentText("Call from: $phoneNumberFormatted")

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, notifBuilder.build())
        }

        builder?.setMessage("Call from: $phoneNumberFormatted")?.setTitle("Incoming call")

        val dialog: AlertDialog? = builder?.create()
//        dialog?.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog?.show()
    }
}