package com.andreasgift.cryptonews.model

import com.google.gson.annotations.SerializedName

data class News (
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("source") var source: String? = null
    )