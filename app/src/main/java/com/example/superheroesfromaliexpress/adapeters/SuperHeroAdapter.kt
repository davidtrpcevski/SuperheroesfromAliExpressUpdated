package com.example.superheroesfromaliexpress.adapeters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.crazylegend.kotlinextensions.glide.GlideApp
import com.crazylegend.kotlinextensions.views.gone
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.listeners.onClickListeners
import com.example.superheroesfromaliexpress.model.SuperHeroModel
/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
class SuperHeroAdapter(private val context: Context) : ListAdapter<SuperHeroModel, SuperHeroViewHolder>(SuperHeroDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        return SuperHeroViewHolder(LayoutInflater.from(context).inflate(R.layout.itemview_super_hero, parent, false))
    }

    var onClickListeners: onClickListeners<SuperHeroModel>? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = "Name: ".plus(item.name)
        holder.gender.text = "Gender: ".plus(item.appearance.gender)

        GlideApp.with(context)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(item.images.lg)
                .into(holder.image)

        holder.itemView.setOnClickListener {
            onClickListeners?.clickedItem(item)
        }

        holder.race.gone()
    }
}