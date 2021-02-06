package com.example.myappwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast

lateinit var myPlayer: MediaPlayer
var flagSum = 0
var imageCount = 0
var musicCount = 0

class MediaAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


     // Picture
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == context?.getString(R.string.action1)) {

            val widgetViews = RemoteViews(context?.packageName, R.layout.media_app_widget)
            if (imageCount == 0) {
                imageCount++
                widgetViews.setImageViewResource(R.id.imageView, R.drawable.guitar1)
                Toast.makeText(context, "Image set to guitar 1", Toast.LENGTH_SHORT).show()
            } else {
                imageCount--
                widgetViews.setImageViewResource(R.id.imageView, R.drawable.guitar2)
                Toast.makeText(context, "Image set to guitar 2", Toast.LENGTH_SHORT).show()
            }
            val widgetComponent = context?.let { ComponentName(it, MediaAppWidget::class.java) }
            val widgetManager = AppWidgetManager.getInstance(context)

            //View update
            widgetManager.updateAppWidget(widgetComponent, widgetViews)

        }
        // music play //If action intent of musicplay String it goes through it
        if (intent?.action == context?.getString(R.string.musicplay)) {
            Toast.makeText(context, "Music started", Toast.LENGTH_SHORT).show()

            // creating Mediaplayer only once
            if (flagSum == 0) {
                myPlayer = MediaPlayer.create(context, R.raw.gloria)
                flagSum++
                musicCount++
            }
            myPlayer.start()
        }
        // music stop  //the same /\
        if (intent?.action == context?.getString(R.string.musicstop)) {
            Toast.makeText(context, "Stop Music", Toast.LENGTH_SHORT).show()
            myPlayer.pause()
        }
        // music next //the same /\
        if (intent?.action == context?.getString(R.string.musicnext)) {
            Toast.makeText(context, "Go for next", Toast.LENGTH_SHORT).show()
            myPlayer.stop()
            if (musicCount == 0) {
                myPlayer = MediaPlayer.create(context, R.raw.gloria)
                musicCount++
            } else if (musicCount == 1) {
                myPlayer = MediaPlayer.create(context, R.raw.grateful)
                musicCount--
            }
            myPlayer.start()
        }
    }

}

//Update Widget View
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)

    // Construct the RemoteViews object // WWW
    val views = RemoteViews(context.packageName, R.layout.media_app_widget)
    views.setTextViewText(R.id.widget_tv, widgetText)

    val intentWWW = Intent(Intent.ACTION_VIEW)
    intentWWW.data = Uri.parse("https://www.google.com")
    val pendingWWW = PendingIntent.getActivity(
        context,
        0,
        intentWWW,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt1, pendingWWW)


    //// Creating broadcast intent picture
    val intentAction = Intent(context.getString(R.string.action1))
    intentAction.component = ComponentName(context, MediaAppWidget::class.java)
    val pendingAction = PendingIntent.getBroadcast(
        context,
        0,
        intentAction,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt2, pendingAction)

    //// start //// Music
    val intentMusicStart = Intent(context.getString(R.string.musicplay))
    intentMusicStart.component = ComponentName(context, MediaAppWidget::class.java)
    val pendingMusicStart = PendingIntent.getBroadcast(
        context,
        0,
        intentMusicStart,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.bt_start, pendingMusicStart)

    //// stop //// Music
    val intentStop = Intent(context.getString(R.string.musicstop))
    intentStop.component = ComponentName(context, MediaAppWidget::class.java)
    val pendingStop = PendingIntent.getBroadcast(
        context,
        0,
        intentStop,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.bt_stop, pendingStop)

    //// next //// Music
    val intentMusicNextSong = Intent(context.getString(R.string.musicnext))
    intentMusicNextSong.component = ComponentName(context, MediaAppWidget::class.java)
    val pendingMusicNextSong = PendingIntent.getBroadcast(
        context,
        0,
        intentMusicNextSong,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.bt_next,pendingMusicNextSong)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}