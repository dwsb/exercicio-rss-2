package br.ufpe.cin.if710.rss.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import br.ufpe.cin.if710.rss.model.ItemRSS
import android.content.ContentResolver
import android.media.browse.MediaBrowser
import android.net.Uri
import android.net.Uri.withAppendedPath
import org.jetbrains.anko.db.insert


class SQLiteRSSHelper(context : Context) : SQLiteOpenHelper(context, "rssDB", null, 1) {
    var db: SQLiteOpenHelper? = null

    companion object {
        val _ID = "_id";
        val TITLE = "title"
        val DATE = "pubDate"
        val DESCRIPTION = "description"
        val LINK = "guid"
        val UNREAD = "unread"
        val ITEMS_TABLE = "items"

        val ALL_COLUMNS = arrayOf(_ID, TITLE, DATE, DESCRIPTION, LINK, UNREAD)
        val BASE_RSS_URI = Uri.parse("content://br.ufpe.cin.if1001.rss/")
        val ITEMS_LIST_URI = Uri.withAppendedPath(BASE_RSS_URI, ITEMS_TABLE)
        val CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/RssProvider.data.text"
        val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/RssProvider.data.text"
    }

    val CREATE_DB_COMMAND : String = "CREATE TABLE " +ITEMS_TABLE+ " (" +
            _ID +" integer primary key autoincrement, "+
            TITLE + " text not null, " +
            DATE + " text not null, " +
            DESCRIPTION + " text not null, " +
            LINK + " text not null, " +
            UNREAD + " boolean not null);"


    override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_COMMAND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Não é usado
    }

    fun getInstance(context: Context): SQLiteOpenHelper? {
        if(db == null){
            db = SQLiteRSSHelper(context.applicationContext)
        }
        return db
    }

    fun insertItem(item : ItemRSS){
        var aux= this.writableDatabase
        aux.insert(ITEMS_TABLE, TITLE to item.title, DATE to item.pubDate,
                DESCRIPTION to item.description, LINK to item.link, UNREAD to "0" )
    }

    
    fun getItemRSS (): ItemRSS? {
      val aux : ItemRSS? = null
        return aux
    }

    fun getItens(): List<ItemRSS>?{
        val aux:List<ItemRSS>?  = null
        return aux
    }

    fun markAsUnread (link:String): Boolean{
        return true
    }

    fun markAsRead (link:String): Boolean{
        return true
    }

}