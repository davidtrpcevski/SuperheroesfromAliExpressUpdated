package com.example.superheroesfromaliexpress.adapeters

import androidx.recyclerview.widget.DiffUtil
import com.example.superheroesfromaliexpress.model.SuperHeroModel

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class SuperHeroDiffUtil : DiffUtil.ItemCallback<SuperHeroModel>() {
    override fun areItemsTheSame(oldItem: SuperHeroModel, newItem: SuperHeroModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SuperHeroModel, newItem: SuperHeroModel): Boolean {
        return oldItem == newItem
    }

}
