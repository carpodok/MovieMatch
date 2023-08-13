package com.example.moviematchapp.data.remote.models.response.cast

import com.google.gson.annotations.SerializedName

data class SingleCastResponse(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("gender")
    val gender: Int? = null,

    @SerializedName("profile_path")
    val profilePath: String? = null,

    @SerializedName("character")
    val character: String? = null,
)
