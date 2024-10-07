package no.hiof.reciperiot.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import no.hiof.reciperiot.R

class NotifcationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val service = NotificationService(context)
        service.showNotification(user = R.string.username.toString())
    }

}