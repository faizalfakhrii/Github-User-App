package com.faizalfakh.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite(
    var username: String? = "",
    var name: String? = "",
    var type: String? = "",
    var avatar_url: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: String? = "",
    var followers: String? = "",
    var following: String? = "",
    var isFavorite: String? = ""
) : Parcelable