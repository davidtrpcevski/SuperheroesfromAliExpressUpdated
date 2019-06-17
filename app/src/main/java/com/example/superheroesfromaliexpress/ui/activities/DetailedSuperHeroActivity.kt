package com.example.superheroesfromaliexpress.ui.activities

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crazylegend.kotlinextensions.context.cancelNotification
import com.crazylegend.kotlinextensions.context.showBackButton
import com.crazylegend.kotlinextensions.core.exhaustive
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.core.INTENT_TAG_DETAILED_HERO
import com.example.superheroesfromaliexpress.core.SuperHeroAbstractActivity
import com.example.superheroesfromaliexpress.model.SuperHeroModel
import com.example.superheroesfromaliexpress.viewmodels.DetailedSuperHeroVM
import com.example.superheroesfromaliexpress.viewmodels.factories.DetailedSuperHeroVMFactory
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_detailed_super_hero.*

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class DetailedSuperHeroActivity : SuperHeroAbstractActivity() {

    private var detailedSuperHeroVM: DetailedSuperHeroVM? = null
    private lateinit var linearLayout: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var detailedImageView: AppCompatImageView

    override fun disposeResources() {
    }

    override fun setView(): Int = R.layout.activity_detailed_super_hero

    override fun initView() {
        detailedImageView = detailedImage
        toolbar = toolbarasdf
        setSupportActionBar(toolbar)
        showBackButton()
        linearLayout = addToThisLayout
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun initLateInitVars() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra(INTENT_TAG_DETAILED_HERO, -1)
        if (id != -1) {
            detailedSuperHeroVM = ViewModelProviders.of(this, DetailedSuperHeroVMFactory(application, id))
                .get(DetailedSuperHeroVM::class.java)
            cancelNotification(id)
        }

        detailedSuperHeroVM?.data?.observe(this, Observer {
            it?.apply {
                when (this) {
                    is RetrofitResult.Success -> {
                        handleSuccess(value)
                    }
                    RetrofitResult.Loading -> {
                        handleLoading()
                    }
                    RetrofitResult.NoData -> {
                    }
                    RetrofitResult.EmptyData -> {

                    }
                    is RetrofitResult.Error -> {
                        handlError(this)
                    }
                    is RetrofitResult.ApiError -> {

                    }
                }.exhaustive
            }
        })
    }

    private fun handlError(error: RetrofitResult.Error) {

    }

    private fun handleLoading() {

    }

    @SuppressLint("SetTextI18n")
    private fun createCardView(texts: HashMap<String, String>, titleString : String) {
        val llparams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val card = MaterialCardView(this)
        card.layoutParams = llparams
        card.useCompatPadding = true
        card.radius = 16f
        card.elevation = 3F
        card.updateLayoutParams<LinearLayout.LayoutParams> {
            marginStart = 16
            marginEnd = 16
            topMargin = 16
            bottomMargin = 16
        }
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        ll.layoutParams = llparams

        val title = TextView(this)
        title.layoutParams = llparams
        title.setPadding(6)
        title.updateLayoutParams<LinearLayout.LayoutParams> {
            marginStart = 16
            marginEnd = 16
            topMargin = 16
            bottomMargin = 16
        }
        title.text = titleString
        title.textSize = 18f
        title.setTypeface(null, Typeface.BOLD)
        ll.addView(title)


        texts.forEach {
            val key = it.key
            val value = it.value
            val text = TextView(this)
            text.layoutParams = llparams
            text.setPadding(6)
            text.updateLayoutParams<LinearLayout.LayoutParams> {
                marginStart = 16
                marginEnd = 16
                topMargin = 8
                bottomMargin = 8
            }
            text.text = "$key $value"
            ll.addView(text)
        }

        card.addView(ll)
        linearLayout.addView(card)

    }

    private fun handleSuccess(model: SuperHeroModel) {
        loadImgNoCache(model.images.lg, detailedImageView)

        val firstCard: HashMap<String, String> = HashMap()
        val secondCard: HashMap<String, String> = HashMap()
        val thirdCard: HashMap<String, String> = HashMap()

        firstCard["Name:"] = model.name
        firstCard["Slug:"] = model.slug
        firstCard["Gender:"] = model.appearance.gender
        firstCard["Race:"] = model.appearance.race.toString()
        firstCard["Height:"] = model.appearance.height[1]
        firstCard["Weight:"] = model.appearance.weight[1]

        secondCard["Intelligence:"] = model.powerstats.intelligence.toString()
        secondCard["Strength:"] = model.powerstats.strength.toString()
        secondCard["Speed:"] = model.powerstats.speed.toString()
        secondCard["Durability:"] = model.powerstats.durability.toString()
        secondCard["Power:"] = model.powerstats.power.toString()
        secondCard["Combat:"] = model.powerstats.combat.toString()

        thirdCard["Comic:"] = model.biography.firstAppearance
        thirdCard["Publisher:"] = model.biography.publisher

        createCardView(firstCard, "Overall")
        createCardView(secondCard, "Power Stats")
        createCardView(thirdCard, "Appears in")

    }
}
