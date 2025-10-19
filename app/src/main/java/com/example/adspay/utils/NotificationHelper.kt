package com.example.adspay.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.adspay.R
import android.util.Log
import com.example.adspay.MainActivity
import android.app.PendingIntent
import android.content.Intent

object NotificationHelper {

    private const val DEFAULT_CHANNEL_ID = "adspay_channel"
    private const val DEFAULT_CHANNEL_NAME = "General Notifications"

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi umum aplikasi Adspay"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Menampilkan notifikasi sistem Android secara aman.
     *
     * @param title Judul notifikasi
     * @param message Pesan notifikasi
     * @param notificationId ID unik notifikasi (default random)
     * @param smallIconResource Ikon kecil di status bar (default logo app)
     */
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = (1000..9999).random(),
        smallIconResource: Int = R.drawable.ic_adspay_logo
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val builder = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(smallIconResource)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } else {
             Log.w("NotificationHelper", "Permission POST_NOTIFICATIONS belum diberikan")
        }
    }
}
