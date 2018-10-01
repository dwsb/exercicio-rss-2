package br.ufpe.cin.if710.rss.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.ufpe.cin.if710.rss.ui.MainActivity

class DReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            MainActivity.exibirFeed(context)
        }
    }
}