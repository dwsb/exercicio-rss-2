package br.ufpe.cin.if710.rss.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.util.DReceiver
import br.ufpe.cin.if710.rss.util.LoadFeed
import br.ufpe.cin.if710.rss.util.ParserRSS
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
class MainActivity : AppCompatActivity()  {

    var RSS_FEED:String = ""
    private val dReceiver = DReceiver()


    companion object {
        const val USERNAME = "rss"
        var listFeedAdapter: ListRssAdapter?=null
        private var db:SQLiteRSSHelper?=null

        //Baixa o xml do rss
        @Throws(IOException::class)
        fun getRssFeed(feed:String):String {
            var inputStream: InputStream? = null
            var rssFeed: String
            try{
                val url= URL(feed)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                inputStream = conn.getInputStream()
                val out = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var count:Int = inputStream.read(buffer)
                while (count != -1) {
                    out.write(buffer, 0, count)
                    count = inputStream.read(buffer)
                }
                val response = out.toByteArray()
                rssFeed = String(response, charset("UTF-8"))
            }finally {
                if(inputStream != null) {
                    inputStream.close()
                }
            }
            return rssFeed
        }
        //Aqui é carregado os itens do rss que estão armazenado no banco e depois é montado o fee
        fun exibirFeed(context:Context) {
            doAsync {
                if (db ==null){
                    db =SQLiteRSSHelper.getInstance(context)
                }
                val items= db!!.getItens()
                uiThread{
                    Toast.makeText(context,"FEED CARREGADO", Toast.LENGTH_LONG).show()
                    listFeedAdapter!!.itens=items
                    listFeedAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listFeedAdapter = ListRssAdapter(emptyList(), this.applicationContext)
        conteudoRSS.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL, false)
        conteudoRSS.adapter = listFeedAdapter
        RSS_FEED = getString(R.string.rssfeed)

    }

    //Aqui é tratado o corpo do xml e o parse retorna uma lista de Itens do RSS
    fun async(url:String){
        doAsync {

            val xml = getRssFeed(url)
            val list = ParserRSS.parse(xml)
            uiThread {
                //modifica a lista do adapter
                listFeedAdapter!!.itens = list
                listFeedAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val prefence= PreferenceManager.getDefaultSharedPreferences(this)
        val link= prefence.getString(USERNAME,RSS_FEED)
        val intent=Intent(this, LoadFeed::class.java)
        intent.putExtra("link",link)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(dReceiver)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter= IntentFilter(LoadFeed.FEED_LOADED)
        registerReceiver(dReceiver,intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mainmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.prefs -> {
                startActivity(Intent(this@MainActivity, PreferenceActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
