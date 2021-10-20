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

class FollowersViewModel : ViewModel(){
    val listUserFollowers = MutableLiveData<ArrayList<User>>()
    private val userService = ApiClient.getClient()

    fun setListUserFollowers(username: String){
        val call = userService.getDetailUserFollower(username)
        call.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.body() != null){
                    val listFollowers = ArrayList<User>()
                    listFollowers.addAll(response.body()!!)
                    listUserFollowers.postValue(listFollowers)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("User Follower Error", "${t.message}")
            }
        })
    }

    fun getListUserFollowers() : LiveData<ArrayList<User>> = listUserFollowers
}