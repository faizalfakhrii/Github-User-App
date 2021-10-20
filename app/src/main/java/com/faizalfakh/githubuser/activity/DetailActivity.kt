package com.faizalfakh.githubuser.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.faizalfakh.githubuser.R
import com.faizalfakh.githubuser.adapter.PageAdapter
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FAVORITE
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.TYPE
import com.faizalfakh.githubuser.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME
import com.faizalfakh.githubuser.helper.QueryHelper
import com.faizalfakh.githubuser.model.User
import com.faizalfakh.githubuser.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.toolbar


class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDetailViewModel: UserDetailViewModel

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var dbHelper: QueryHelper
    private lateinit var dataUser: User
    private var statusFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UserDetailViewModel::class.java
        )

        dataUser = intent.getParcelableExtra(EXTRA_USER) as User

        userDetailViewModel.setUserDetail(dataUser.username!!)
        showLoading(true)
        userDetailViewModel.getUserDetail().observe(this, { userDetail ->
            if (userDetail != null) {
                Glide.with(applicationContext).load(userDetail.avatar_url).into(ivAvatar)
                tvUsername.text = userDetail.username
                tvName.text = userDetail.name
                if (userDetail.company != null) tvCompany.text =
                    userDetail.company else tvCompany.visibility = View.GONE
                if (userDetail.location != null) tvLocation.text =
                    userDetail.company else tvLocation.visibility = View.GONE
                tvRepository.text = userDetail.repository
                showLoading(false)
            }
        })

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

        val cursor: Cursor = dbHelper.queryByUsername(dataUser.username.toString())
        if (cursor.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(true)
        }

        fab_favorite.setOnClickListener(this)

        val pageAdapter = PageAdapter(this, supportFragmentManager)
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setStatusFavorite(status: Boolean) {
        if (status) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) loadingProgress.visibility = View.VISIBLE else loadingProgress.visibility = View.INVISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        menu!!.findItem(R.id.favorite).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> Toast.makeText(applicationContext, "Favorite", Toast.LENGTH_SHORT)
                .show()
            R.id.settings -> startActivity(Intent(this, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        val data = userDetailViewModel.getUserDetail()
        when (v?.id) {
            R.id.fab_favorite -> {
                if (statusFavorite) {
                    val idUser = data.value?.username.toString()
                    dbHelper.deleteByUsername(idUser)
                    Toast.makeText(this, getString(R.string.user_deleted), Toast.LENGTH_SHORT).show()
                    setStatusFavorite(false)
                    statusFavorite = false
                } else {
                    val values = ContentValues()
                    values.put(USERNAME, data.value?.username)
                    values.put(NAME, data.value?.name)
                    values.put(TYPE, dataUser.type)
                    values.put(AVATAR, data.value?.avatar_url)
                    values.put(COMPANY, data.value?.company)
                    values.put(LOCATION, data.value?.location)
                    values.put(REPOSITORY, data.value?.repository)
                    values.put(FOLLOWERS, data.value?.followers)
                    values.put(FOLLOWING, data.value?.following)
                    values.put(FAVORITE, "isFav")

                    statusFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, getString(R.string.user_added), Toast.LENGTH_SHORT).show()
                    setStatusFavorite(true)
                }
            }
        }
    }
}