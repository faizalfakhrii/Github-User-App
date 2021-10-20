package com.faizalfakh.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.adapter.UserAdapter
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.hasFixedSize()
        rvUser.adapter = adapter

        svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query.isNullOrEmpty()) {
                    false
                } else {
                    userViewModel.setUserSearch(query)
                    showLoading(true)
                    if(!userViewModel.isInternetAvailable(applicationContext)){
                        showLoading(false)
                        Toast.makeText(this@MainActivity, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show()
                    }
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        userViewModel.getUserSearch().observe(this@MainActivity, { userList ->
            if (userList.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.no_user_found), Toast.LENGTH_SHORT).show()
            } else {
                adapter.setData(userList)
            }
            showLoading(false)
        })


        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if(state) loadingProgress.visibility = View.VISIBLE else loadingProgress.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.settings -> startActivity(Intent(this, SettingActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}