package com.faizalfakh.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.activity.DetailActivity
import com.faizalfakh.githubuser.adapter.UserAdapter
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*


class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = activity!!.intent.getParcelableExtra(EXTRA_USER) as User
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvUserFollowing.layoutManager = LinearLayoutManager(context)
        rvUserFollowing.hasFixedSize()
        rvUserFollowing.adapter = adapter

        followingViewModel.setListUserFollowing(data.username.toString())
        followingViewModel.getListUserFollowing().observe(this, { userFollowing ->
            if(userFollowing.isNullOrEmpty()){
                rvUserFollowing.visibility = View.INVISIBLE
                no_user.visibility = View.VISIBLE
            }else{
                no_user.visibility = View.GONE
                adapter.setData(userFollowing)
            }
        })

        adapter.setOnItemClickCallback(object  : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val detailIntent = Intent(context, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(detailIntent)
            }
        })
    }
}