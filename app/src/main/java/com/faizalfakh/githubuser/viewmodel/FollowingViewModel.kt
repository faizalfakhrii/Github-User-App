package com.faizalfakh.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listUserFollowing = MutableLiveData<ArrayList<User>>()
    private val userService = ApiClient.getClient()

    fun setListUserFollowing(username: String){
        val call = userService.getDetailUserFollowing(username)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.body() != null){
                    val listFollowers = ArrayList<User>()
                    listFollowers.addAll(response.body()!!)
                    listUserFollowing.postValue(listFollowers)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("User Following Error", "${t.message}")
            }
        })
    }

    fun getListUserFollowing() : LiveData<ArrayList<User>> = listUserFollowing
}