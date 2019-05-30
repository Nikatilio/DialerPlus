package com.enzo.dialerplus

import android.content.Context
import android.os.Handler
import android.util.Log
import java.util.*
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent




class CallReceiver: PhonecallReceiver() {

    private val TAG = CallReceiver::class.simpleName

    private lateinit var listener: (String?) -> Unit

    override fun onIncomingCallReceived(ctx: Context, number: String?, start: Date) {
        Log.d(TAG, "Date: $start. Number: $number")

        val scheduledIntent = Intent(ctx, AlertAlarmActivity::class.java)
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        ctx.startActivity(scheduledIntent)

        listener(number)
    }

    override fun onIncomingCallAnswered(ctx: Context, number: String?, start: Date) {
    }

    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {
    }

    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
    }

    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
    }

    fun setListener(listener: (String?) -> Unit) {
        this.listener = listener
    }
}