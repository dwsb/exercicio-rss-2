package br.ufpe.cin.if710.rss.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.ui.MainActivity

class SReceiver: BroadcastReceiver() {
    //Recebe notificação quando um novo notícia é identificada
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent_main=Intent(context, MainActivity::class.java)
        val pendingIntent=PendingIntent.getActivity(context,0,intent_main,0)
        val builder=NotificationCompat.Builder(context!!, "0")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Novas Notícias Chegaram")
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(0,builder.build())
    }
}