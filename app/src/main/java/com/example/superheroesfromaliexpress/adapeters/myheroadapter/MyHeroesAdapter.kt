package com.example.superheroesfromaliexpress.adapeters.myheroadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.crazylegend.kotlinextensions.glide.GlideApp
import com.crazylegend.kotlinextensions.views.visible
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.adapeters.SuperHeroViewHolder
import com.example.superheroesfromaliexpress.database.MyHeroModel
import com.example.superheroesfromaliexpress.listeners.onClickListeners

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class MyHeroesAdapter(private val context: Context) : ListAdapter<MyHeroModel, SuperHeroViewHolder>(MyHeroDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.itemview_super_hero, parent, false)

        return SuperHeroViewHolder(view)
    }

    var onClickListeners: onClickListeners<MyHeroModel>? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {

        val item = getItem(position) // model instance

        holder.race.visible()

        holder.name.text = "Name: ".plus(item.name)
        holder.gender.text = "Gender: ".plus(item.gender)
        holder.race.text = "Race: ".plus(item.race)

        GlideApp.with(context)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(item.image)
                .into(holder.image)

        holder.itemView.setOnLongClickListener {
            onClickListeners?.clickedItem(item)
            true
        }
    }


}

class MyHeroDiffUtil : DiffUtil.ItemCallback<MyHeroModel>() {
    override fun areItemsTheSame(oldItem: MyHeroModel, newItem: MyHeroModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyHeroModel, newItem: MyHeroModel): Boolean {
        return oldItem.id == newItem.id
    }
}