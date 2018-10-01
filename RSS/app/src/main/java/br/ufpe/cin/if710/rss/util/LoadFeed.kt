package br.ufpe.cin.if710.rss.util

import android.app.IntentService
import android.content.Intent
import android.util.Log
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.model.ItemRSS
import br.ufpe.cin.if710.rss.ui.MainActivity
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class LoadFeed : IntentService("CarregaFeed") {
    var db: SQLiteRSSHelper?=null
    companion object {
        val FEED_LOADED="br.ufpe.cin.if710.rss.FEED_LOADED"
        val INSERTED_DATA="br.ufpe.cin.if710.rss.INSERTED_DATA"
    }
    //Aqui é carregado no banco os itens captados no XML
    override fun onHandleIntent(intent: Intent?) {
        var db = SQLiteRSSHelper(this.applicationContext)
        val auxlink= intent!!.getStringExtra("link")
        try {
            val xml= MainActivity.getRssFeed(auxlink)
            val list = ParserRSS.parse(xml)

            for (item: ItemRSS in list){
                val auxItem= db.getItemRSS(item.link)
                if (auxItem==null){
                    sendBroadcast(Intent(INSERTED_DATA))
                    db!!.insertItem(item)
                }
            }
            //Notifica a MainActivity que os itens já estão todos carregados
            sendBroadcast(Intent(FEED_LOADED))
        }
        catch (io: IOException){
            io.printStackTrace()
        }catch (e: XmlPullParserException){
            e.printStackTrace()
        }
        finally {
            db!!.close()
        }

    }


}