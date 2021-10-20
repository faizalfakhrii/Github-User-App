package com.faizalfakh.githubuser.service

import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.model.UserDetail
import com.faizalfakh.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("search/users")
    fun searchUser(@Query("q" ) username : String) : Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username : String) : Call<UserDetail>

    @GET("users/{username}/following")
    fun getDetailUserFollowing(@Path("username") username: String) : Call<List<User>>

    @GET("users/{username}/followers")
    fun getDetailUserFollower(@Path("username") username: String) : Call<List<User>>
}