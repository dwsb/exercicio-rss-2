package br.ufpe.cin.if710.rss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.model.ItemRSS
import kotlinx.android.synthetic.main.itemlista.view.*

class ListRssAdapter (var itens :List<ItemRSS>, private val context: Context) :
        RecyclerView.Adapter<ListRssAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = itens.get(position)
        holder.bindView(item)

        holder.itemView.setOnClickListener{
            //Marca no banco quando a notícia é lida
            val db=SQLiteRSSHelper.getInstance(context.applicationContext)
            db.markAsRead(item.link)
            //Abre a notícia no navegador
            val uri = Uri.parse(item.link)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlista,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ItemRSS) {
            itemView.item_titulo.text=item.title
            itemView.item_data.text=item.pubDate
        }
    }
}
