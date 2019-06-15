package com.example.superheroesfromaliexpress.model

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
data class SuperHeroModel(
    val appearance: Appearance,
    val biography: Biography,
    val connections: Connections,
    val id: Int,
    val images: Images,
    val name: String,
    val powerstats: Powerstats,
    val slug: String,
    val work: Work
) {
    data class Appearance(
        val eyeColor: String,
        val gender: String,
        val hairColor: String,
        val height: List<String>,
        val race: String?,
        val weight: List<String>
    )

    data class Work(
        val base: String,
        val occupation: String
    )

    data class Powerstats(
        val combat: Int,
        val durability: Int,
        val intelligence: Int,
        val power: Int,
        val speed: Int,
        val strength: Int
    )

    data class Biography(
        val aliases: List<String>,
        val alignment: String,
        val alterEgos: String,
        val firstAppearance: String,
        val fullName: String,
        val placeOfBirth: String,
        val publisher: String
    )

    data class Connections(
        val groupAffiliation: String,
        val relatives: String
    )

    data class Images(
        val lg: String,
        val md: String,
        val sm: String,
        val xs: String
    )
}