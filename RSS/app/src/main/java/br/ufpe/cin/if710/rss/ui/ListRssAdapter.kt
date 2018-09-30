package br.ufpe.cin.if710.rss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.model.ItemRSS
import kotlinx.android.synthetic.main.itemlista.view.*

class ListRssAdapter(var rss :List<ItemRSS>, private val context: Context):
        RecyclerView.Adapter<ListRssAdapter.ViewHolder>(){


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rss.get(position)
        holder.bindView(item)
        //Cria o listener associado a acao para abrir o link no navegador
        holder.itemView.setOnClickListener{
            val uri = Uri.parse(item.link)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
    //Seta parametros de layout da view, para posterioment permitir a associacao dos elementos da lista na view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlista,parent,false)
        return ViewHolder(view)
    }
    //retorna tamanho da lista
    override fun getItemCount(): Int {
        return rss.size
    }
    //Ligação dos elementos da textView com os elementos da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ItemRSS) {
            itemView.item_titulo.text = item.title
            itemView.item_data.text = item.pubDate
        }
    }
}