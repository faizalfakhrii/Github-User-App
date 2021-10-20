package com.faizalfakh.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.activity.DetailActivity
import com.faizalfakh.githubuser.adapter.UserAdapter
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = activity!!.intent.getParcelableExtra(EXTRA_USER) as User
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvUserFollowers.layoutManager = LinearLayoutManager(context)
        rvUserFollowers.hasFixedSize()
        rvUserFollowers.adapter = adapter

        followersViewModel.setListUserFollowers(data.username.toString())
        followersViewModel.getListUserFollowers().observe(this, {userFollowers ->
            if(userFollowers.isNullOrEmpty()){
                rvUserFollowers.visibility = View.GONE
                no_user.visibility = View.VISIBLE
            }else{
                no_user.visibility = View.GONE
                adapter.setData(userFollowers)
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