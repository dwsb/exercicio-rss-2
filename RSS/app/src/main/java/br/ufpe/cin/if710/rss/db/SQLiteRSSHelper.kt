package br.ufpe.cin.if710.rss.db
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.ufpe.cin.if710.rss.model.ItemRSS
import android.content.ContentValues
import android.database.Cursor
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.insert


class SQLiteRSSHelper(ctx:Context) : ManagedSQLiteOpenHelper(ctx, "if710", null, 1) {

    companion object {
        val ITENS_TABLE = "items"
        val _ID= "_id"
        val TITLE = "title"
        val DATE = "date"
        val DESCRIPTION = "description"
        val LINK = "link"
        val READ ="unread"
        val columns = arrayOf(_ID, TITLE, DATE, DESCRIPTION, LINK, READ)

        private var db: SQLiteRSSHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SQLiteRSSHelper {
            if (db == null) {
                db = SQLiteRSSHelper(ctx.applicationContext)
            }
            return db!!
        }
    }

    val CREATE_DB_COMMAND : String = "CREATE TABLE " +ITENS_TABLE+ " (" +
            _ID +" integer primary key autoincrement, "+
            TITLE + " text not null, " +
            DATE + " text not null, " +
            DESCRIPTION + " text not null, " +
            LINK + " text not null, " +
            READ + " text not null);"

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(CREATE_DB_COMMAND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    val Context.database: SQLiteRSSHelper get() = SQLiteRSSHelper.getInstance(getApplicationContext())
    //Insere itens ao banco
    fun insertItem(item: ItemRSS){
        val db = this.writableDatabase
        db.insert(ITENS_TABLE, TITLE to item.title, DATE to item.pubDate,
                DESCRIPTION to item.description, LINK to item.link, READ to item.read)
    }
    //Consulta intens através do link
    fun getItemRSS(link:String): ItemRSS? {
        var aux: ItemRSS?=null
        val c: Cursor =this.readableDatabase.query(ITENS_TABLE, columns,LINK+" = ?",arrayOf(link),
                null,null,null)
        while (c.moveToNext()){
            var auxtitle=c.getString(c.getColumnIndexOrThrow(TITLE))
            var auxlink=c.getString(c.getColumnIndexOrThrow(LINK))
            var auxdate=c.getString(c.getColumnIndexOrThrow(DATE))
            var auxdescription=c.getString(c.getColumnIndexOrThrow(DESCRIPTION))
            var auxread=c.getString(c.getColumnIndexOrThrow(READ))

            aux = ItemRSS(auxtitle, auxlink, auxdate, auxdescription, auxread)
        }

        return aux

    }
    //Retorna todos os elementos que não forem marcados como lido
    fun getItens():List<ItemRSS>{
        var aux = ArrayList<ItemRSS>()
        val query="select * from "+ ITENS_TABLE+" where "+READ+" = "+"0"
        val c= this.readableDatabase.rawQuery(query,null)

        while (c.moveToNext()){
            var auxtitle=c.getString(c.getColumnIndexOrThrow(TITLE))
            var auxlink=c.getString(c.getColumnIndexOrThrow(LINK))
            var auxdate=c.getString(c.getColumnIndexOrThrow(DATE))
            var auxdescription=c.getString(c.getColumnIndexOrThrow(DESCRIPTION))
            var auxread=c.getString(c.getColumnIndexOrThrow(READ))

            if (auxread.equals("0")) {
                aux.add(ItemRSS(auxtitle, auxlink, auxdate, auxdescription, auxread))
            }
        }
        return aux

    }
    //marca o item do rss como lido no banco
    fun markAsRead(link:String):Boolean{
        var aux=false
        val item = getItemRSS(link)
        val data:ContentValues= ContentValues()
        if (item != null) {
            data.put(TITLE,item.title)
            data.put(LINK,item.link)
            data.put(DATE,item.pubDate)
            data.put(DESCRIPTION,item.description)
            data.put(READ,"1")
            val up:Int=this.writableDatabase.update(ITENS_TABLE,data,LINK+ " = ?", arrayOf(link))
            aux = (up==1)
        }
        return aux
    }
    //marca como não lido o item do rss no banco
    fun markAsUnRead(link:String):Boolean{
        var aux=false
        val item = getItemRSS(link)
        val data:ContentValues= ContentValues()
        if (item != null) {
            data.put(TITLE,item.title)
            data.put(LINK,item.link)
            data.put(DATE,item.pubDate)
            data.put(DESCRIPTION,item.description)
            data.put(READ,"0")
            val up:Int=this.writableDatabase.update(ITENS_TABLE,data,LINK+ " = ?", arrayOf(link))
            aux = (up==1)
        }
        return aux

    }
}
