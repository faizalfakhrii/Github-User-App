package com.faizalfakh.consumerapp.helper

import android.database.Cursor
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.FAVORITE
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.TYPE
import com.faizalfakh.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME
import com.faizalfakh.consumerapp.model.UserFavorite


object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserFavorite> {

        val favoriteList = ArrayList<UserFavorite>()

        cursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(USERNAME))
                val name =
                    getString(getColumnIndexOrThrow(NAME))
                val type =
                    getString(getColumnIndexOrThrow(TYPE))
                val avatar =
                    getString(getColumnIndexOrThrow(AVATAR))
                val company =
                    getString(getColumnIndexOrThrow(COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(LOCATION))
                val repository =
                    getString(getColumnIndexOrThrow(REPOSITORY))
                val followers =
                    getString(getColumnIndexOrThrow(FOLLOWERS))
                val following =
                    getString(getColumnIndexOrThrow(FOLLOWING))
                val isFavorite =
                    getString(getColumnIndexOrThrow(FAVORITE))

                favoriteList.add(
                    UserFavorite(
                        username,
                        name,
                        type,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        isFavorite
                    )
                )
            }
        }

        cursor?.close()
        return favoriteList
    }
}