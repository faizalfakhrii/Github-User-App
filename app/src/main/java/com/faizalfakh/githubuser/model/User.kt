package com.faizalfakh.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("login") var username : String? = null,
    @SerializedName("type") var type : String? = null,
    @SerializedName("avatar_url") var avatar_url : String? = null
) : Parcelable