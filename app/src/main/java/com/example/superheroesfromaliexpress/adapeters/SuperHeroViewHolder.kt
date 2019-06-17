package com.example.superheroesfromaliexpress.adapeters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemview_super_hero.view.*

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class SuperHeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.itvsh_image
    val name = itemView.itvsh_name
    val gender = itemView.itvsh_gender
    val race = itemView.itvsh_race
}
