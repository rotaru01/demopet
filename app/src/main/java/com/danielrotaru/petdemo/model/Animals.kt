package com.danielrotaru.petdemo.model

import java.io.Serializable

data class Animals(
    val animals: List<Animal>,
    val pagination: Pagination?,
)

data class AnimalObject(
    val animal: List<Animal>
)

data class Animal(
    val id: Int,
    val name: String,
    val breeds: Breeds?,
    val species: String,
    val size: String,
    val gender: String,
    val status: String,
    val distance: Double,
    val photos: List<Photo>,
) : Serializable

data class Breeds(
    val primary: String,
    val secondary: String,
    val mixed: Boolean,
    val unknown: Boolean,
) : Serializable

data class Photo(
    val full: String,
    val small: String,
    val medium: String
) : Serializable

data class Pagination(
    val countPerPage: Int,
    val totalCount: Int,
    val currentPage: Int,
    val totalPages: Int,
) : Serializable
