package com.example.moviematchapp.data.remote.models.response.cast

import com.google.gson.annotations.SerializedName

data class CastsResponse(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("cast")
    val cast: List<SingleCastResponse>? = null,
)