package com.danielrotaru.petdemo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AnimalsRemote(
    @SerializedName("animals")
    val animals: List<AnimalRemote>?,
    @SerializedName("pagination")
    val pagination: PaginationRemote?,
) : Serializable

data class AnimalObjectRemote(
    @SerializedName("animals")
    val animals: List<AnimalRemote>?,
)

data class AnimalRemote(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("breeds")
    val breeds: BreedsRemote?,
    @SerializedName("species")
    val species: String?,
    @SerializedName("size")
    val size: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("distance")
    val distance: Double?,
    @SerializedName("photos")
    val photos: List<PhotoRemote>?,
) : Serializable

data class BreedsRemote(
    @SerializedName("primary")
    val primary: String?,
    @SerializedName("secondary")
    val secondary: String?,
    @SerializedName("mixed")
    val mixed: Boolean?,
    @SerializedName("unknown")
    val unknown: Boolean?,
) : Serializable

data class PhotoRemote(
    @SerializedName("full")
    val full: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("medium")
    val medium: String?,
) : Serializable

data class PaginationRemote(
    @SerializedName("count_per_page")
    val countPerPage: Int?,
    @SerializedName("total_count")
    val totalCount: Int?,
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,

    ) : Serializable

fun AnimalsRemote?.toDomain(): Animals {
    return Animals(
        animals = this?.animals?.map { it.toDomain() } ?: emptyList(),
        pagination = this?.pagination?.toDomain()
    )
}

fun AnimalRemote?.toDomain(): Animal {
    return Animal(
        id = this?.id ?: -1,
        name = this?.name.orEmpty(),
        breeds = this?.breeds?.toDomain(),
        species = this?.species.orEmpty(),
        size = this?.size.orEmpty(),
        gender = this?.gender.orEmpty(),
        status = this?.status.orEmpty(),
        distance = this?.distance ?: -1.0,
        photos = this?.photos?.map { it.toDomain() } ?: emptyList()
    )
}

fun AnimalObjectRemote?.toDomain(): AnimalObject {
    return AnimalObject(
        animal = this?.animals?.map { it.toDomain() } ?: emptyList(),
    )
}

fun BreedsRemote?.toDomain(): Breeds {
    return Breeds(
        primary = this?.primary.orEmpty(),
        secondary = this?.secondary.orEmpty(),
        mixed = this?.mixed ?: false,
        unknown = this?.unknown ?: false
    )
}

fun PhotoRemote?.toDomain(): Photo {
    return Photo(
        full = this?.full.orEmpty(),
        small = this?.small.orEmpty(),
        medium = this?.medium.orEmpty()
    )
}

fun PaginationRemote?.toDomain(): Pagination {
    return Pagination(
        countPerPage = this?.countPerPage ?: -1,
        totalCount = this?.totalCount ?: -1,
        currentPage = this?.currentPage ?: -1,
        totalPages = this?.totalPages ?: -1
    )
}
