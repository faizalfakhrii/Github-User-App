package com.faizalfakh.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faizalfakh.githubuser.model.UserDetail
import com.faizalfakh.githubuser.service.ApiClient
import com.faizalfakh.githubuser.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel(){

    val userDetail = MutableLiveData<UserDetail>()
    private val userService : UserService = ApiClient.getClient()

    fun setUserDetail(username: String){
        val call = userService.getDetailUser(username)
        call.enqueue(object : Callback<UserDetail>{
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.body() != null){
                    val detail = UserDetail(
                        username = response.body()!!.username,
                        name = response.body()!!.name,
                        avatar_url = response.body()!!.avatar_url,
                        location = response.body()!!.location,
                        company = response.body()!!.company,
                        repository = response.body()!!.repository,
                        followers = response.body()!!.followers,
                        following = response.body()!!.following
                    )
                    userDetail.postValue(detail)
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Log.d("User Detail Error", "${t.message}")
            }

        })
    }

    fun getUserDetail(): LiveData<UserDetail>{
        return userDetail
    }

}