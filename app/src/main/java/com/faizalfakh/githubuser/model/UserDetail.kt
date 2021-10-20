package com.faizalfakh.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail (
    @SerializedName("login") val username : String?,
    @SerializedName("name") var name : String?,
    @SerializedName("avatar_url") val avatar_url : String?,
    @SerializedName("location") val location : String?,
    @SerializedName("company") val company : String?,
    @SerializedName("public_repos") val repository : String?,
    @SerializedName("followers") val followers: String?,
    @SerializedName("following") val following: String?
) : Parcelable