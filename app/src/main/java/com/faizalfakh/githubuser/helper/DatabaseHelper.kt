package com.faizalfakh.githubuser.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FAVORITE
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.TYPE
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
)  {

    companion object {
        private var mInstance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper? {
            if (mInstance == null) {
                mInstance = DatabaseHelper(context.applicationContext)
            }
            return mInstance
        }

        private const val DATABASE_NAME = "UserGithub"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($USERNAME TEXT NOT NULL," +
                " $NAME TEXT," +
                " $TYPE TEXT," +
                " $AVATAR TEXT NOT NULL," +
                " $COMPANY TEXT," +
                " $LOCATION TEXT," +
                " $REPOSITORY TEXT NOT NULL," +
                " $FOLLOWERS TEXT NOT NULL," +
                " $FOLLOWING TEXT NOT NULL," +
                " $FAVORITE TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}