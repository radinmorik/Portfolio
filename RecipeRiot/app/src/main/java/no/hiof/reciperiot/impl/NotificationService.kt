package no.hiof.reciperiot.impl

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import no.hiof.reciperiot.MainActivity
import no.hiof.reciperiot.R

class NotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(user: String) {
        val actvityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            actvityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, FIRST_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_fastfood_24)
            .setContentTitle("Your first Login!")
            .setContentText("Welcome to RecipeRiot $user!")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            1, notification
        )
    }

    companion object {
        const val FIRST_CHANNEL_ID = "Notification_channel01"
    }

}