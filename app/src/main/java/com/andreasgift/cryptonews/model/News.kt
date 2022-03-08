package com.andreasgift.cryptonews.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news_table")
data class News (
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("source") var source: String? = null,
    @PrimaryKey(autoGenerate = true) var id: Int
    )